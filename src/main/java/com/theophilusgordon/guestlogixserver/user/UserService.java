package com.theophilusgordon.guestlogixserver.user;

import com.theophilusgordon.guestlogixserver.exception.BadRequestException;
import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserResponse updateUser(String id, UpdateUserRequest request) {
        var user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        if(request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if(request.getMiddleName() != null)
            user.setMiddleName(request.getMiddleName());
        if(request.getLastName() != null)
            user.setLastName(request.getLastName());
        if(request.getPhone() != null)
            user.setPhone(request.getPhone());
        if(request.getProfilePhotoUrl() != null)
            user.setProfilePhotoUrl(request.getProfilePhotoUrl());
        repository.save(user);

        return this.buildUserResponse(user);
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadRequestException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }

    public UserResponse getUser(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        return this.buildUserResponse(user);
    }

    public Iterable<UserResponse> getUsers() {
        return repository.findAll().stream()
                .map(this::buildUserResponse)
                .toList();
    }

    public void deleteUser(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        repository.deleteById(id);
    }

    private UserResponse buildUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .company(user.getCompany())
                .role(user.getRole())
                .build();
    }
}
