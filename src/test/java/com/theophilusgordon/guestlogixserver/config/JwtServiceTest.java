package com.theophilusgordon.guestlogixserver.config;

import com.theophilusgordon.guestlogixserver.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//TODO: Fix this test class
class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenExtractUsername_thenCorrect() {
        String token = "mockToken";
        String expectedUsername = "mockUsername";
        when(jwtService.extractUsername(token)).thenReturn(expectedUsername);

        String actualUsername = jwtService.extractUsername(token);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void whenGenerateToken_thenCorrect() {
        String expectedToken = "mockToken";
        when(userDetails.getUsername()).thenReturn("mockUsername");
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        String actualToken = jwtService.generateToken(userDetails);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void whenGenerateRefreshToken_thenCorrect() {
        String expectedToken = "mockRefreshToken";
        when(userDetails.getUsername()).thenReturn("mockUsername");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn(expectedToken);

        String actualToken = jwtService.generateRefreshToken(userDetails);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void whenIsTokenValid_thenCorrect() {
        String token = "mockToken";
        when(userDetails.getUsername()).thenReturn("mockUsername");
        when(jwtService.extractUsername(token)).thenReturn("mockUsername");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }
}