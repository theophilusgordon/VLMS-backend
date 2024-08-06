package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inviteUser_UserAlreadyExists_ThrowsBadRequestException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserInviteRequest request = new UserInviteRequest("test@example.com", "HOST");

        assertThrows(BadRequestException.class, () -> userService.inviteUser(request));
    }

    @Test
    void inviteUser_ValidRequest_Success() throws MessagingException {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserInviteRequest request = new UserInviteRequest("test@example.com", "HOST");

        UserInviteResponse response = userService.inviteUser(request);

        assertNotNull(response);
        verify(emailService, times(1)).sendInvitationMail(anyString(), anyString(), anyString());
    }

    @Test
    void forgotPassword_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.requestResetPassword("test@example.com"));
    }

    @Test
    void forgotPassword_ValidEmail_Success() throws MessagingException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        userService.requestResetPassword("test@example.com");

        verify(emailService, times(1)).sendForgotPasswordMail(anyString(), anyString(), anyString());
    }

    @Test
    void resetPassword_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        PasswordResetRequest request = new PasswordResetRequest("newPassword", "newPassword");

        assertThrows(NotFoundException.class, () -> userService.resetPassword(UUID.randomUUID().toString(), request));
    }

    @Test
    void resetPassword_PasswordsDoNotMatch_ThrowsBadRequestException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));

        PasswordResetRequest request = new PasswordResetRequest("newPassword", "differentPassword");

        assertThrows(BadRequestException.class, () -> userService.resetPassword(UUID.randomUUID().toString(), request));
    }

    @Test
    void resetPassword_ValidRequest_Success() throws MessagingException {
        User user = new User();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        PasswordResetRequest request = new PasswordResetRequest("newPassword", "newPassword");

        userService.resetPassword(UUID.randomUUID().toString(), request);

        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendPasswordResetSuccessMail(anyString(), anyString(), anyString());
    }

    @Test
    void updateUser_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UserUpdateRequest request = new UserUpdateRequest(
                "John",
                "Dee",
                "Doe",
                "+25457746612",
                "ljahgl"
        );

        assertThrows(NotFoundException.class, () -> userService.updateUser(UUID.randomUUID().toString(), request));
    }

    @Test
    void updateUser_ValidRequest_Success() {
        User user = new User();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest("John",
                "Dee",
                "Doe",
                "+25457746612",
                "ljahgl");

        userService.updateUser(UUID.randomUUID().toString(), request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changePassword_WrongCurrentPassword_ThrowsBadRequestException() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        PasswordChangeRequest request = new PasswordChangeRequest("wrongPassword", "newPassword", "newPassword");

        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);

        assertThrows(BadRequestException.class, () -> userService.changePassword(request, principal));
    }

    @Test
    void changePassword_PasswordsDoNotMatch_ThrowsBadRequestException() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "newPassword", "differentPassword");

        UsernamePasswordAuthenticationToken principal = mock(UsernamePasswordAuthenticationToken.class);
        when(principal.getPrincipal()).thenReturn(user);

        assertThrows(BadRequestException.class, () -> userService.changePassword(request, principal));
    }

    @Test
    void changePassword_ValidRequest_Success() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "newPassword", "newPassword");

        Principal principal = mock(UsernamePasswordAuthenticationToken.class);
        when(((UsernamePasswordAuthenticationToken) principal).getPrincipal()).thenReturn(user);

        userService.changePassword(request, principal);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUser_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(UUID.randomUUID().toString()));
    }

    @Test
    void getUser_ValidId_Success() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));

        User user = userService.getUser(UUID.randomUUID().toString());

        assertNotNull(user);
    }

    @Test
    void getUsers_ValidRequest_Success() {
        Page<User> users = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(users);

        Page<User> result = userService.getUsers(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchHosts_ValidRequest_Success() {
        Page<User> users = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAllByRoleAndFullNameContaining(any(Role.class), anyString(), any(PageRequest.class))).thenReturn(users);

        Page<User> result = userService.searchHosts("query", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getHosts_ValidRequest_Success() {
        Page<User> users = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAllByRole(any(Role.class), any(PageRequest.class))).thenReturn(users);

        Page<User> result = userService.getHosts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getTotalHosts_ValidRequest_Success() {
        when(userRepository.countByRole(any(Role.class))).thenReturn(1);

        Integer totalHosts = userService.getTotalHosts();

        assertEquals(1, totalHosts);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(UUID.randomUUID().toString()));
    }

    @Test
    void deleteUser_ValidId_Success() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(true);

        userService.deleteUser(UUID.randomUUID().toString());

        verify(userRepository, times(1)).deleteById(any(UUID.class));
    }
}
