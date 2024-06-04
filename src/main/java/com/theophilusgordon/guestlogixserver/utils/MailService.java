package com.theophilusgordon.guestlogixserver.utils;

import com.theophilusgordon.guestlogixserver.guest.Guest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Async
    public void sendInvitationMail(String to, String subject, String userId) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();

        configureMimeMessageHelper(
                mimeMessage,
                to,
                from,
                subject,
                context,
                "invitation-mail-template"
        );

        String invitationUrl = frontendUrl + "/register/" + userId;

        context.setVariable("subject", subject);
        context.setVariable("invitationUrl", invitationUrl);

        mailSender.send(mimeMessage);
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
}
