package com.theophilusgordon.guestlogixserver.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUpdateUser_thenUserIsUpdated() {
        String id = "mockId";
        UpdateUserRequest request = new UpdateUserRequest(
                "Jane",
                "Doe",
                "Smith",
                "0987654321",
                "http://example.com/profile2.jpg"
        );

        ResponseEntity<User> response = userController.updateUser(id, request);

        verify(userService, times(1)).updateUser(id, request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void whenChangePassword_thenPasswordIsChanged() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "oldPassword",
                "newPassword",
                "newPassword"
        );

        ResponseEntity<?> response = userController.changePassword(request, principal);

        verify(userService, times(1)).changePassword(request, principal);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void whenGetConnectedUser_thenUserIsReturned() {
        User user = new User();
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);

        ResponseEntity<User> response = userController.getConnectedUser(authenticationToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void whenGetUser_thenUserIsReturned() {
        String id = "mockId";
        UserDto userDto = new UserDto(
                "mockId",
                "John",
                "Doe",
                "Smith",
                "1234567890",
                "john@example.com",
                "http://example.com/profile.jpg",
                "Company",
                Role.ADMIN
        );
        when(userService.getUser(id)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUser(id);

        verify(userService, times(1)).getUser(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void whenGetUsers_thenUsersAreReturned() {
        Iterable<UserDto> users = new ArrayList<>();
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<Iterable<UserDto>> response = userController.getUsers();

        verify(userService, times(1)).getUsers();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() {
        String id = "mockId";

        ResponseEntity<?> response = userController.deleteUser(id);

        verify(userService, times(1)).deleteUser(id);
        assertEquals(200, response.getStatusCodeValue());
    }
}