package com.theophilusgordon.guestlogixserver.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void whenUpdateUser_thenUserIsUpdated() {
        String id = "mockId";
        UserUpdateRequest request = new UserUpdateRequest(
                "Jane",
                "Doe",
                "Smith",
                "0987654321",
                "http://example.com/profile2.jpg"
        );

        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.updateUser(id, request);

        verify(userRepository, times(1)).save(user);
    }

//TODO:    fix this test
    @Test
    void whenChangePassword_thenPasswordIsChanged() {
        String currentPassword = "oldPassword";
        PasswordChangeRequest request = new PasswordChangeRequest(
                currentPassword,
                "newPassword",
                "newPassword"
        );

        User user = new User();
        user.setPassword(passwordEncoder.encode(currentPassword));
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);

        userService.changePassword(request, authenticationToken);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenGetUser_thenUserIsReturned() {
        String id = "mockId";

        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.getUser(id);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void whenGetUsers_thenUsersAreReturned() {
        userService.getUsers();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() {
        String id = "mockId";
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}