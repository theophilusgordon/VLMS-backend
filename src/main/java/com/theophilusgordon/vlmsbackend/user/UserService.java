package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.MailConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.token.Token;
import com.theophilusgordon.vlmsbackend.token.TokenRepository;
import com.theophilusgordon.vlmsbackend.token.TokenType;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    public UserInviteResponse inviteUser(UserInviteRequest request) {
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.email())))
            throw new BadRequestException(ExceptionConstants.USER_ALREADY_EXISTS + request.email());

        var user = User.builder()
                .email(request.email())
                .role(this.createRole(request.role()))
                .status(Status.INVITED)
                .build();
        var savedUser = userRepository.save(user);
        var otp = generateAndSaveActivationToken(user);
        emailService.sendActivationEmail(
                user.getEmail(),
                otp
        );
        return UserInviteResponse.builder()
                .id(String.valueOf(savedUser.getId()))
                .email(savedUser.getEmail())
                .role(String.valueOf(savedUser.getRole()))
                .status(savedUser.getStatus())
                .build();
    }

    public void requestResetPassword(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
        var otp = generateAndSaveActivationToken(user);

        emailService.sendRequestPasswordResetEmail(
                user.getEmail(),
                otp
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
        emailService.sendPasswordResetSuccessEmail(user.getEmail());
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

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode();
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .tokenType(TokenType.OTP)
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
