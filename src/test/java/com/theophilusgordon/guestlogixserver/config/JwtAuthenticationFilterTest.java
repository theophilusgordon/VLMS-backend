package com.theophilusgordon.guestlogixserver.config;

import com.theophilusgordon.guestlogixserver.token.Token;
import com.theophilusgordon.guestlogixserver.token.TokenRepository;
import com.theophilusgordon.guestlogixserver.utils.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRequestPathContainsApiV1Auth_thenDoFilter() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/v1/auth");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenUserEmailIsNull_thenDoFilter() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/v1/auth");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUsername(anyString())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenTokenIsInvalid_thenDoFilter() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/v1/auth");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUsername(anyString())).thenReturn("userEmail");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenTokenIsValid_thenDoFilter() throws ServletException, IOException {
        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getServletPath()).thenReturn("/api/v1/auth");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUsername(anyString())).thenReturn("userEmail");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}