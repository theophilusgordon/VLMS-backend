package com.theophilusgordon.vlmsbackend.utils.email;

import com.theophilusgordon.vlmsbackend.exception.EmailSendFailureException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.security.userdetailsservice.UserDetailsServiceImpl;
import com.theophilusgordon.vlmsbackend.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void sendSignupSuccessMail(String to, String subject, String username) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();

        configureMimeMessageHelper(
                mimeMessage,
                to,
                from,
                subject,
                context,
                "signup-success-mail-template"
        );

        context.setVariable("subject", subject);
        context.setVariable("username", username);

        mailSender.send(mimeMessage);
    }

    public void sendForgotPasswordMail(String to,
                                       String subject,
                                       String userId
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();

        configureMimeMessageHelper(
                mimeMessage,
                to,
                from,
                subject,
                context,
                "forgot-password-mail-template"
        );

        String resetPasswordUrl = frontendUrl + "/reset-password/" + userId;

        context.setVariable("subject", subject);
        context.setVariable("resetPasswordUrl", resetPasswordUrl);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendPasswordResetSuccessMail(String to,
                                             String subject,
                                             String username
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();

        configureMimeMessageHelper(
                mimeMessage,
                to,
                from,
                subject,
                context,
                "password-reset-success-mail-template"
        );

        context.setVariable("subject", subject);
        context.setVariable("username", username);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendCheckinNotificationMail(String to,
                                            String subject,
                                            String hostname,
                                            Guest guest,
                                            LocalDateTime checkinTime
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();

        configureMimeMessageHelper(
                mimeMessage,
                to,
                from,
                subject,
                context,
                "checkin-notification-mail-template"
        );

        context.setVariable("subject", subject);
        context.setVariable("guest", guest);
        context.setVariable("hostname", hostname);
        context.setVariable("checkinTime", checkinTime);

        mailSender.send(mimeMessage);
    }

    public void sendCheckinSuccessMail(String to,
                                       String subject,
                                       String guestName,
                                       byte[] attachment
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("guestName", guestName);

        String htmlContent = templateEngine.process("guest-checkin-success-mail-template", context);
        mimeMessageHelper.setText(htmlContent, true);

        ByteArrayResource file = new ByteArrayResource(attachment);
        mimeMessageHelper.addAttachment("QRCode.png", file);

        mailSender.send(mimeMessage);
    }

    private void configureMimeMessageHelper(
            MimeMessage mimeMessage,
            String to,
            String from,
            String subject,
            Context context,
            String template
    ) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String htmlContent = templateEngine.process(template, context);
        mimeMessageHelper.setText(htmlContent, true);
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
        properties.put("type", emailDetails.getType());
        properties.put("actor", emailDetails.getName());
        properties.put("reason", emailDetails.getReason());
        properties.put("date", emailDetails.getDate());

        Context context = new Context();
        context.setVariables(properties);
        return context;
    }
}
