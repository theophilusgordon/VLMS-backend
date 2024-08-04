package com.theophilusgordon.vlmsbackend.security.jwt;

import com.theophilusgordon.vlmsbackend.security.userdetailsservice.UserDetailsImpl;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private final UserRepository userRepository;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User user
    ) {
        return buildToken(extraClaims, user.getEmail());
    }

    public String generateRefreshToken(
            User user
    ) {
        return buildToken(new HashMap<>(), user.getEmail());
    }

    public String buildToken(Map<String, Object> extraClaims, String email) {
//        TODO: refactor orElseThrow to handle exception
        User user = userRepository.findByEmail(email).orElseThrow();

        List<String> authorities = new UserDetailsImpl(user).getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        extraClaims.put("full_name", user.getFullName());
        extraClaims.put("role", authorities);
        return Jwts
                .builder()
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .signWith(getSignInKey())
                .claim("user_id", user.getId())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .audience().add("vlms.theophilusgordon.com")
                .and()
                .issuer("VLMS API")
                .subject(user.getEmail())
                .compact();
    }


//    public boolean isTokenValid(String token, User user) {
//        final String username = extractUsername(token);
//        return (username.equals(user.getUsername())) && !isTokenExpired(token);
//    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("user_id", String.class)));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
