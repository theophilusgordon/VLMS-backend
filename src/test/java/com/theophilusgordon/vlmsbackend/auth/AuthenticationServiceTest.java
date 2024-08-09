package com.theophilusgordon.vlmsbackend.auth;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.security.jwt.JwtService;
import com.theophilusgordon.vlmsbackend.security.userdetailsservice.UserDetailsServiceImpl;
import com.theophilusgordon.vlmsbackend.token.TokenService;
import com.theophilusgordon.vlmsbackend.user.Status;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import com.theophilusgordon.vlmsbackend.token.TokenRepository;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private EmailService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActivateAccount_Success() {
        AccountActivationRequest authRequest = new AccountActivationRequest(
                "First", "Middle",
                "Lastname", "", "test@example.com",
                "password",
                "password",
                "123456");
        User invitedUser = new User();
        invitedUser.setStatus(Status.INVITED);

        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.of(invitedUser));
        when(tokenRepository.existsByUserAndToken(invitedUser, authRequest.otp())).thenReturn(true);
        when(passwordEncoder.encode(authRequest.password())).thenReturn("encodedPassword");

        authenticationService.activateAccount(authRequest);

        assertEquals(Status.ACTIVATED, invitedUser.getStatus());
        verify(userRepository).save(invitedUser);
        verify(emailService).sendActivatedEmail(invitedUser.getEmail());
    }

    @Test
    void testActivateAccount_UserNotInvited() {
        AccountActivationRequest authRequest = new AccountActivationRequest(
                "First", "Middle",
                "Lastname", "", "test@example.com",
                "password",
                "password",
                "123456");
        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authenticationService.activateAccount(authRequest));
        assertEquals(ExceptionConstants.USER_NOT_INVITED + authRequest.email(), exception.getMessage());
    }

    @Test
    void testActivateAccount_UserAlreadyActivated() {
        AccountActivationRequest authRequest = new AccountActivationRequest(
                "First", "Middle",
                "Lastname", "", "test@example.com",
                "password",
                "password",
                "123456");
        User invitedUser = new User();
        invitedUser.setStatus(Status.ACTIVATED);

        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.of(invitedUser));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authenticationService.activateAccount(authRequest));
        assertEquals(ExceptionConstants.USER_ALREADY_ACTIVATED + authRequest.email(), exception.getMessage());
    }

    @Test
    void testActivateAccount_InvalidActivationCode() {
        AccountActivationRequest authRequest = new AccountActivationRequest(
                "First", "Middle",
                "Lastname", "", "test@example.com",
                "password",
                "password",
                "123456");        User invitedUser = new User();
        invitedUser.setStatus(Status.INVITED);

        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.of(invitedUser));
        when(tokenRepository.existsByUserAndToken(invitedUser, authRequest.otp())).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authenticationService.activateAccount(authRequest));
        assertEquals(ExceptionConstants.INVALID_ACTIVATION_CODE, exception.getMessage());
    }

    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        User user = new User();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(any(HashMap.class), any(UserDetails.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("refreshToken");

        AuthenticationResponse authResponse = authenticationService.authenticate(authRequest);

        assertNotNull(authResponse);
        assertEquals("jwtToken", authResponse.getAccessToken());
        assertEquals("refreshToken", authResponse.getRefreshToken());
    }

   @Test
void testRefreshToken_Success() throws IOException {
    String refreshToken = "refreshToken";
    String userEmail = "test@example.com";
    String accessToken = "newAccessToken";
    User user = new User();

    ServletOutputStream outputStream = mock(ServletOutputStream.class);
    when(response.getOutputStream()).thenReturn(outputStream);
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
    when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
    when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(user);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
    when(jwtService.generateToken(user)).thenReturn(accessToken);

    authenticationService.refreshToken(request, response);

    verify(jwtService).generateToken(user);
    verify(tokenService).saveUserToken(user, accessToken);
    verify(response).getOutputStream();
}

    @Test
    void testRefreshToken_InvalidToken() throws IOException {
        String refreshToken = "invalidToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(null);

        authenticationService.refreshToken(request, response);

        verify(jwtService, never()).generateToken(any(UserDetails.class));
        verify(tokenService, never()).saveUserToken(any(User.class), anyString());
        verify(response, never()).getOutputStream();
    }
}
