package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.token.Token;
import com.theophilusgordon.vlmsbackend.token.TokenRepository;
import com.theophilusgordon.vlmsbackend.token.TokenType;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Token token;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .role(Role.HOST)
                .status(Status.ACTIVATED)
                .password("password")
                .build();

        token = Token.builder()
                .token("123456")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .tokenType(TokenType.OTP)
                .user(user)
                .build();
    }

    @Test
    void testInviteUser_UserAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserInviteRequest request = new UserInviteRequest("test@example.com", "host");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.inviteUser(request));
        assertEquals(ExceptionConstants.USER_ALREADY_EXISTS + request.email(), exception.getMessage());
    }

    @Test
    void testInviteUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        UserInviteRequest request = new UserInviteRequest("test@example.com", "host");

        userService.inviteUser(request);

        verify(emailService, times(1)).sendActivationEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testRequestResetPassword_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.requestResetPassword("test@example.com"));
        assertEquals("User with id test@example.com not found", exception.getMessage());
    }

    @Test
    void testRequestResetPassword_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        userService.requestResetPassword("test@example.com");

        verify(emailService, times(1))
                .sendRequestPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testResetPassword_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "123456", "newPassword", "newPassword");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.resetPassword(request));
        assertEquals("User with id test@example.com not found", exception.getMessage());
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.existsByUserAndToken(any(User.class), anyString())).thenReturn(false);

        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "123456", "newPassword", "newPassword");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.resetPassword(request));
        assertEquals(ExceptionConstants.INVALID_TOKEN, exception.getMessage());
    }

    @Test
    void testResetPassword_PasswordsMismatch() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.existsByUserAndToken(any(User.class), anyString())).thenReturn(true);

        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "123456", "newPassword", "differentPassword");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.resetPassword(request));
        assertEquals(ExceptionConstants.PASSWORDS_MISMATCH, exception.getMessage());
    }

    @Test
    void testResetPassword_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.existsByUserAndToken(any(User.class), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        PasswordResetRequest request = new PasswordResetRequest(
                "test@example.com",
                "newPassword",
                "newPassword",
                "newPassword");

        userService.resetPassword(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1))
                .sendPasswordResetSuccessEmail(eq("test@example.com"));
    }

    @Test
    void testUpdateUser() {
        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserUpdateRequest request = new UserUpdateRequest("Francisca",
                "Aikins",
                "Baffoe",
                "0555555555");

        User updatedUser = userService.updateUser(principal, request);

        assertEquals(user, updatedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword_IncorrectCurrentPassword() {
        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        PasswordChangeRequest request = new PasswordChangeRequest("wrongCurrentPassword", "newPassword", "newPassword");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.changePassword(request, principal));
        assertEquals(ExceptionConstants.INCORRECT_PASSWORD, exception.getMessage());
    }

    @Test
    void testChangePassword_PasswordsMismatch() {
        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "newPassword", "differentPassword");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.changePassword(request, principal));
        assertEquals(ExceptionConstants.PASSWORDS_MISMATCH, exception.getMessage());
    }

    @Test
    void testChangePassword_Success() {
        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "newPassword", "newPassword");

        userService.changePassword(request, principal);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendPasswordResetSuccessEmail(eq("test@example.com"));
    }

    @Test
    void testGetUser_UserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUser(UUID.randomUUID().toString()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void testGetUser_Success() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(UUID.randomUUID().toString());

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testGetUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.getUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSearchHosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAllByRoleAndFullNameContaining(any(Role.class), anyString(), any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.searchHosts("query", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetHosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAllByRole(any(Role.class), any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.getHosts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetTotalHosts() {
        when(userRepository.countByRole(any(Role.class))).thenReturn(10);

        Integer totalHosts = userService.getTotalHosts();

        assertNotNull(totalHosts);
        assertEquals(10, totalHosts);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(UUID.randomUUID().toString()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(true);

        userService.deleteUser(UUID.randomUUID().toString());

        verify(userRepository, times(1)).deleteById(any(UUID.class));
    }
}
