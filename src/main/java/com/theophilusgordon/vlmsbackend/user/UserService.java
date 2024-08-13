package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.token.Token;
import com.theophilusgordon.vlmsbackend.token.TokenRepository;
import com.theophilusgordon.vlmsbackend.token.TokenType;
import com.theophilusgordon.vlmsbackend.utils.Encoder;
import com.theophilusgordon.vlmsbackend.utils.S3Service;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Service s3Service;

    public void inviteUser(UserInviteRequest request) {
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.email())))
            throw new BadRequestException(ExceptionConstants.USER_ALREADY_EXISTS + request.email());

        var user = User.builder()
                .email(request.email())
                .role(this.createRole(request.role()))
                .status(Status.INVITED)
                .build();
        userRepository.save(user);
        var otp = generateAndSaveActivationToken(user);
        emailService.sendActivationEmail(
                user.getEmail(),
                otp
        );
    }

    public void requestResetPassword(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
        var otp = generateAndSaveActivationToken(user);

        emailService.sendRequestPasswordResetEmail(
                user.getEmail(),
                otp
        );
    }

    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User", request.email()));

        if(!tokenRepository.existsByUserAndToken(user, request.otp()))
            throw new BadRequestException(ExceptionConstants.INVALID_TOKEN);

        if (!request.password().equals(request.confirmPassword()))
            throw new BadRequestException(ExceptionConstants.PASSWORDS_MISMATCH);

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        emailService.sendPasswordResetSuccessEmail(user.getEmail());
    }

    public User updateUser(Principal principal, UserUpdateRequest request) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        userRepository.save(UserMapper.userUpdateRequestToUser(user, request));
        return user;
    }

    public String updateUserProfilePhoto(Principal principal, MultipartFile profilePhoto) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        s3Service.deleteImage(user.getProfilePhoto());
        String encodedImage = Encoder.encodeImageToBase64(profilePhoto);
        String profilePhotoUrl = s3Service.uploadEncodedImage(encodedImage);
        user.setProfilePhoto(profilePhotoUrl);
        userRepository.save(user);
        return profilePhotoUrl;
    }

    public void changePassword(PasswordChangeRequest request, Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionConstants.INCORRECT_PASSWORD);
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new BadRequestException(ExceptionConstants.PASSWORDS_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);

        emailService.sendPasswordResetSuccessEmail(user.getEmail());
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", String.valueOf(id)));
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

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User", String.valueOf(id));
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
