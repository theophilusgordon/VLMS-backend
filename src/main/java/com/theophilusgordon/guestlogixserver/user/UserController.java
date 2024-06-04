package com.theophilusgordon.guestlogixserver.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/invite")
    public ResponseEntity<UserInviteResponse> inviteUser(@RequestBody UserInviteRequest request) throws MessagingException {
        return ResponseEntity.ok(userService.inviteUser(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable String id, @RequestBody ResetPasswordRequest request) throws MessagingException {
        userService.resetPassword(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getConnectedUser(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserResponse userResponse = this.convertUserToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    private UserResponse convertUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setMiddleName(user.getMiddleName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setProfilePhotoUrl(user.getProfilePhotoUrl());
        userResponse.setRole(user.getRole());
        return userResponse;
    }
}
