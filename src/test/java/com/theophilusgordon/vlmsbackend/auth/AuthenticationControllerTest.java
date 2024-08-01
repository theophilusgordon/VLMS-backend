package com.theophilusgordon.vlmsbackend.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegister_thenServiceIsCalled() throws MessagingException {
        RegisterRequest registerRequest = new RegisterRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        when(service.register(registerRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> result = controller.register(registerRequest);

        verify(service, times(1)).register(registerRequest);
        assertEquals(ResponseEntity.ok(authenticationResponse), result);
    }

    @Test
    void whenAuthenticate_thenServiceIsCalled() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        when(service.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> result = controller.authenticate(authenticationRequest);

        verify(service, times(1)).authenticate(authenticationRequest);
        assertEquals(ResponseEntity.ok(authenticationResponse), result);
    }

    @Test
    void whenRefreshToken_thenServiceIsCalled() throws IOException {
        doNothing().when(service).refreshToken(request, response);

        controller.refreshToken(request, response);

        verify(service, times(1)).refreshToken(request, response);
    }
}