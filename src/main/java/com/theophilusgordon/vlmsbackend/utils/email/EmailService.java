package com.theophilusgordon.vlmsbackend.utils.email;

import com.theophilusgordon.vlmsbackend.constants.EmailConstants;
import com.theophilusgordon.vlmsbackend.exception.EmailSendFailureException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
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

    @Value("${spring.mail.username}")
    private String from;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendActivationEmail(String recipient, String code) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.ACTIVATE_ACCOUNT)
                .subject(EmailConstants.ACTIVATE_ACCOUNT_SUBJECT)
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
                .subject(EmailConstants.ACCOUNT_ACTIVATION_SUBJECT)
                .loginUrl(frontendUrl + "/login")
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendRequestPasswordResetEmail(String recipient, String otp) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.REQUEST_PASSWORD_RESET)
                .subject(EmailConstants.REQUEST_RESET_PASSWORD_SUBJECT)
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
                .subject(EmailConstants.PASSWORD_RESET_SUCCESS_SUBJECT)
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
                .subject(EmailConstants.CHECKIN_NOTIFICATION_SUBJECT)
                .hostName(hostName)
                .guestName(guest.getFullName())
                .guestPhone(guest.getPhone())
                .guestCompany(guest.getCompany())
                .checkinTime(checkInTime)
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
                .subject(EmailConstants.CHECKIN_SUCCESS_SUBJECT)
                .guestName(guestName)
                .qrCode(qrCode)
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendCheckoutNotificationEmail(String recipient,
                                             String hostName,
                                             Guest guest,
                                             LocalDateTime checkoutTime) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.CHECKOUT_NOTIFICATION)
                .subject(EmailConstants.CHECKOUT_NOTIFICATION_SUBJECT)
                .hostName(hostName)
                .guestName(guest.getFullName())
                .guestPhone(guest.getPhone())
                .guestCompany(guest.getCompany())
                .checkinTime(checkoutTime)
                .build();

        sendEmail(emailDetails);
    }

    @Async
    public void sendCheckoutSuccessEmail(String recipient,
                                        String guestName,
                                         LocalDateTime checkoutTime) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(recipient)
                .template(EmailTemplate.CHECKOUT_SUCCESS)
                .subject(EmailConstants.CHECKOUT_SUCCESS_SUBJECT)
                .guestName(guestName)
                .checkoutTime(checkoutTime)
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

            Context context = getContext(emailDetails);

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

    private static Context getContext(EmailDetails emailDetails) {
        Map<String, Object> properties = new HashMap<>();

        properties.put("recipient", emailDetails.getRecipient());
        properties.put("otp", emailDetails.getOtp());
        properties.put("confirmationUrl", emailDetails.getActivationUrl());
        properties.put("actor", emailDetails.getName());
        properties.put("activationUrl", emailDetails.getActivationUrl());
        properties.put("loginUrl", emailDetails.getLoginUrl());
        properties.put("resetPasswordUrl", emailDetails.getResetPasswordUrl());
        properties.put("hostName", emailDetails.getHostName());
        properties.put("guestName", emailDetails.getGuestName());
        properties.put("checkInTime", emailDetails.getCheckinTime());
        properties.put("checkOutTime", emailDetails.getCheckoutTime());
        properties.put("qrCode", emailDetails.getQrCode());
        properties.put("guestPhone", emailDetails.getGuestPhone());
        properties.put("guestCompany", emailDetails.getGuestCompany());

        Context context = new Context();
        context.setVariables(properties);
        return context;
    }
}
