package com.theophilusgordon.guestlogixserver.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendInvitationMail(String to, String subject, String invitationLink) throws MessagingException {
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

        context.setVariable("subject", subject);
        context.setVariable("invitationLink", invitationLink);

        mailSender.send(mimeMessage);
    }

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

    public void sendForgotPasswordMail(String to, String subject, String resetPasswordUrl) throws MessagingException {
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

        context.setVariable("subject", subject);
        context.setVariable("resetPasswordUrl", resetPasswordUrl);

        mailSender.send(mimeMessage);
    }


    public void sendMailWithAttachment(String to, String subject, String body, byte[] attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        ByteArrayResource file = new ByteArrayResource(attachment);
        helper.addAttachment("QRCode.png", file);

        mailSender.send(message);
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
