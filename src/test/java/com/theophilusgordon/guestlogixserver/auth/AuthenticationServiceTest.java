package com.theophilusgordon.guestlogixserver.auth;

import com.theophilusgordon.guestlogixserver.user.User;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import com.theophilusgordon.guestlogixserver.token.TokenRepository;
import com.theophilusgordon.guestlogixserver.jwt.JwtService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegister_thenUserIsCreated() throws MessagingException {
        RegisterRequest request = new RegisterRequest("John",
                "Doe",
                "Smith",
                "1234567890",
                "john@example.com",
                "http://example.com/profile.jpg",
                "password",
                "USER");
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        authenticationService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void whenAuthenticate_thenUserIsAuthenticated() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "1234567890");
        // set request fields

        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        authenticationService.authenticate(request);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void whenRefreshToken_thenTokenIsRefreshed() throws IOException {
        when(request.getHeader(anyString())).thenReturn("Bearer jwtToken");
        when(jwtService.extractUsername(anyString())).thenReturn("userEmail");

        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        authenticationService.refreshToken(request, response);

        verify(tokenRepository, times(1)).save(any());
    }
}