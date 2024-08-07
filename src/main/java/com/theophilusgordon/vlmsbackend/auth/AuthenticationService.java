package com.theophilusgordon.vlmsbackend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.security.jwt.JwtService;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.security.userdetailsservice.UserDetailsServiceImpl;
import com.theophilusgordon.vlmsbackend.token.TokenService;
import com.theophilusgordon.vlmsbackend.user.Status;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import com.theophilusgordon.vlmsbackend.token.TokenRepository;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

public void activateAccount(AccountActivationRequest request) {
    User invitedUser = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BadRequestException(ExceptionConstants.USER_NOT_INVITED + request.email()));

    if (invitedUser.getStatus() != Status.INVITED)
        throw new BadRequestException(ExceptionConstants.USER_ALREADY_ACTIVATED + request.email());

    if (!tokenRepository.existsByUserAndToken(invitedUser, request.otp()))
        throw new BadRequestException(ExceptionConstants.INVALID_ACTIVATION_CODE);

    invitedUser.setFirstName(request.firstName());
    invitedUser.setMiddleName(request.middleName());
    invitedUser.setLastName(request.lastName());
    invitedUser.setPhone(request.phone());
    invitedUser.setProfilePhotoUrl(request.profilePhotoUrl());
    invitedUser.setPassword(passwordEncoder.encode(request.password()));
    invitedUser.setStatus(Status.ACTIVATED);
    userRepository.save(invitedUser);

    emailService.sendActivatedEmail(invitedUser.getEmail());
}

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var claims = new HashMap<String, Object>();
        var user =  ((User) auth.getPrincipal());

        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (UserDetails) auth.getPrincipal());
        var refreshToken = jwtService.generateRefreshToken((UserDetails) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                revokeAllUserTokens(user);
                tokenService.saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}