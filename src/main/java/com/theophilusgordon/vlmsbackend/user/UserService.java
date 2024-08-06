package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.MailConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.utils.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public UserInviteResponse inviteUser(UserInviteRequest request) throws MessagingException {
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.email())))
            throw new BadRequestException(ExceptionConstants.USER_ALREADY_EXISTS + request.email());

        var user = User.builder()
                .email(request.email())
                .role(this.createRole(request.role()))
                .build();
        var savedUser = userRepository.save(user);
        mailService.sendInvitationMail(
                user.getEmail(),
                MailConstants.INVITATION_SUBJECT,
                String.valueOf(savedUser.getId())
        );
        return UserInviteResponse.builder()
                .id(String.valueOf(savedUser.getId()))
                .email(savedUser.getEmail())
                .role(String.valueOf(savedUser.getRole()))
                .status(Status.INVITED)
                .build();
    }

    public void requestResetPassword(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
        mailService.sendForgotPasswordMail(
                user.getEmail(),
                MailConstants.REQUEST_RESET_PASSWORD_SUBJECT,
                String.valueOf(user.getId())
        );
    }

    public void resetPassword(String id, PasswordResetRequest request) throws MessagingException {
        var user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("User", id));

        if (!request.password().equals(request.confirmPassword())) {
            throw new BadRequestException(ExceptionConstants.PASSWORDS_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        mailService.sendPasswordResetSuccessMail(
                user.getEmail(),
                MailConstants.PASSWORD_RESET_SUCCESS_SUBJECT,
                user.getFullName()
        );
    }

    public User updateUser(String id, UserUpdateRequest request) {
        var user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("User", id));
        userRepository.save(UserMapper.userUpdateRequestToUser(user, request));
        return user;
    }

    public void changePassword(PasswordChangeRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionConstants.INCORRECT_PASSWORD);
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new BadRequestException(ExceptionConstants.PASSWORDS_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

    public User getUser(String id) {
        return userRepository.findById(UUID.fromString(id))
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
        if (!userRepository.existsById(UUID.fromString(id))) {
            throw new NotFoundException("User", id);
        }
        userRepository.deleteById(UUID.fromString(id));
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
