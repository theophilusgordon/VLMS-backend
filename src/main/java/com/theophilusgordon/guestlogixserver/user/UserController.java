package com.theophilusgordon.guestlogixserver.user;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Invite a user", description = "Invite a user as host")
    @PostMapping("/invite")
    public ResponseEntity<UserInviteResponse> inviteUser(@RequestBody UserInviteRequest request) throws MessagingException {
        return ResponseEntity.ok(userService.inviteUser(request));
    }

    @Operation(summary = "Request password change", description = "Request password change for user")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody PasswordRequestResetRequest request) throws MessagingException {
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password", description = "Reset password for user")
    @PatchMapping("/reset-password/{id}")
    public ResponseEntity<Void> resetPassword(@PathVariable String id, @RequestBody PasswordResetRequest request) throws MessagingException {
        userService.resetPassword(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update user", description = "Update user information")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Change password", description = "Change password for user")
    @PatchMapping
    public ResponseEntity<Void> changePassword(
            @RequestBody PasswordChangeRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get connected user", description = "Get connected user")
    @GetMapping("/me")
    public ResponseEntity<User> getConnectedUser(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user", description = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(summary = "Get all users", description = "Get all users paginated. Default page is 1 and default size is 10")
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sort
            ) {
        Page<User> users = userService.getUsers(page, size, sort);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get hosts", description = "Get all hosts paginated. Default page is 1 and default size is 10")
    @GetMapping("/hosts")
    public ResponseEntity<Page<User>> getHosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sort
    ) {
        Page<User> hosts = userService.getHosts(page, size, sort);
        return ResponseEntity.ok(hosts);
    }

    @GetMapping("/hosts/search")
    public ResponseEntity<Page<User>> searchHosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String query,
            @RequestParam String sort
    ) {
        Page<User> hosts = userService.searchHosts(query, page, size, sort);
        return ResponseEntity.ok(hosts);
    }

    @Operation(summary = "Delete user", description = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
