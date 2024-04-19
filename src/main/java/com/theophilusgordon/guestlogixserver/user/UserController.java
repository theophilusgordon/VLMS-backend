package com.theophilusgordon.guestlogixserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        service.updateUser(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getConnectedUser(Principal connectedUser) {
        return ResponseEntity.ok((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
