package com.theophilusgordon.vlmsbackend.utils.email;

import com.theophilusgordon.vlmsbackend.exception.EmailSendFailureException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.security.userdetailsservice.UserDetailsServiceImpl;
import com.theophilusgordon.vlmsbackend.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendActivationEmail(String recipient, String code) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.ACTIVATE_ACCOUNT)
                .subject("Activate your account")
                .activationUrl(frontendUrl + "/activate-account")
                .otp(code)
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendActivatedEmail(String recipient) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.ACCOUNT_ACTIVATED)
                .subject("Account activated successfully")
                .loginUrl(frontendUrl + "/login")
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendRequestPasswordResetEmail(String recipient, String otp) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.REQUEST_PASSWORD_RESET)
                .subject("Reset your password")
                .otp(otp)
                .resetPasswordUrl(frontendUrl + "/reset-password")
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendPasswordResetSuccessEmail(String recipient) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.PASSWORD_RESET_SUCCESS)
                .subject("Password reset successful")
                .loginUrl(frontendUrl + "/login")
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendCheckinNotificationEmail(String recipient,
                                             String hostName,
                                             Guest guest,
                                             LocalDateTime checkInTime) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.CHECKIN_NOTIFICATION)
                .subject("Password reset successful")
                .hostName(hostName)
                .guestName(guest.getFullName())
                .guestPhone(guest.getPhone())
                .guestCompany(guest.getCompany())
                .checkInTime(checkInTime)
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendCheckinSuccessEmail(String recipient,
                                        String guestName,
                                        byte[] qrCode) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.CHECKIN_SUCCESS)
                .subject("Check-in successful")
                .guestName(guestName)
                .qrCode(qrCode)
                .build();

        sendEmail(emailDetails);
    }

    private void sendEmail(EmailDetails emailDetails) {
        try {
            String templateName = emailDetails.getTemplate().getTemplateName();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(
                    message,
                    MULTIPART_MODE_MIXED,
                    UTF_8.name());

            User user = (User) userDetailsService.loadUserByUsername(emailDetails.getRecipient());
            Context context = getContext(emailDetails, user);

            helper.setFrom(from);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());

            String template = templateEngine.process(templateName, context);

            helper.setText(template, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendFailureException();
        }
    }

    private static Context getContext(EmailDetails emailDetails, User user) {
        Map<String, Object> properties = new HashMap<>();

        properties.put("firstname", user.getFirstName());
        properties.put("recipient", emailDetails.getRecipient());
        properties.put("otp", emailDetails.getOtp());
        properties.put("confirmationUrl", emailDetails.getActivationUrl());
        properties.put("actor", emailDetails.getName());
        properties.put("activationUrl", emailDetails.getActivationUrl());
        properties.put("loginUrl", emailDetails.getLoginUrl());
        properties.put("resetPasswordUrl", emailDetails.getResetPasswordUrl());
        properties.put("hostName", emailDetails.getHostName());
        properties.put("guestName", emailDetails.getGuestName());
        properties.put("checkInTime", emailDetails.getCheckInTime());
        properties.put("qrCode", emailDetails.getQrCode());
        properties.put("guestPhone", emailDetails.getGuestPhone());
        properties.put("guestCompany", emailDetails.getGuestCompany());

        Context context = new Context();
        context.setVariables(properties);
        return context;
    }
}
