package com.theophilusgordon.guestlogixserver.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import com.theophilusgordon.guestlogixserver.token.Token;
import com.theophilusgordon.guestlogixserver.token.TokenRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenLogout_thenTokenIsRevoked() {
        String tokenString = "mockToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenString);

        Token token = new Token();
        token.setToken(tokenString);
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
        verify(tokenRepository, times(1)).save(token);
    }
}