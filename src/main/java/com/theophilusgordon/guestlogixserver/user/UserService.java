package com.theophilusgordon.guestlogixserver.user;

import com.theophilusgordon.guestlogixserver.exception.BadRequestException;
import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.utils.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public UserInviteResponse inviteUser(UserInviteRequest request) throws MessagingException {
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail())))
            throw new BadRequestException(String.format("User with email: %s already exists", request.getEmail()));

        var user = User.builder()
                .email(request.getEmail())
                .role(this.createRole(request.getRole()))
                .build();
        var savedUser = userRepository.save(user);
        mailService.sendInvitationMail(
                user.getEmail(),
                "Invitation to Join Guest Logix as a Host",
                savedUser.getId()
        );
        return UserInviteResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .role(String.valueOf(savedUser.getRole()))
                .status(Status.INVITED)
                .build();
    }

    public void forgotPassword(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
        mailService.sendForgotPasswordMail(
                user.getEmail(),
                "Password Reset Request for Your Guest Logix Account",
                user.getId()
        );
    }

    public void resetPassword(String id, PasswordResetRequest request) throws MessagingException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        mailService.sendPasswordResetSuccessMail(
                user.getEmail(),
                "Password Reset Successful",
                user.getFullName()
        );
    }

    public User updateUser(String id, UserUpdateRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        userRepository.save(UserMapper.userUpdateRequestToUser(user, request));
        return user;
    }

    public void changePassword(PasswordChangeRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadRequestException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    public Page<User> getUsers(Pageable pageable) {
         return userRepository.findAll(pageable);
    }

    public Page<User> searchHosts(String query, Pageable pageable) {
        return userRepository.findAllByRoleAndFullNameContaining(Role.HOST, query, pageable);
    }

    public Page<User> getHosts(Pageable pageable) {
        return userRepository.findAllByRole(Role.HOST, pageable);
    }

    public Integer getTotalHosts(){
        return userRepository.countByRole(Role.HOST);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    private Role createRole(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
