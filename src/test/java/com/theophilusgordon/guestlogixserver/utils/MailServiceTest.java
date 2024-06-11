package com.theophilusgordon.guestlogixserver.utils;

import com.theophilusgordon.guestlogixserver.guest.Guest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class MailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mailService, "from", "test@example.com");

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Some non-null text");
    }

    @Test
    void testSendInvitationMail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendInvitationMail("test@example.com", "Test Subject", "123");

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendForgotPasswordMail() throws MessagingException {
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    mailService.sendForgotPasswordMail("test@example.com", "Test Subject", "123");

    verify(mailSender, times(1)).send(mimeMessage);
}

@Test
void testSendPasswordResetSuccessMail() throws MessagingException {
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    mailService.sendPasswordResetSuccessMail("test@example.com", "Test Subject", "TestUser");

    verify(mailSender, times(1)).send(mimeMessage);
}

@Test
void testSendCheckinNotificationMail() throws MessagingException {
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    Guest guest = new Guest();
    guest.setFirstName("Eric");
    guest.setLastName("Kpabi");
    guest.setPhone("1234567890");
    LocalDateTime checkinTime = LocalDateTime.now();

    mailService.sendCheckinNotificationMail("test@example.com", "Test Subject", "TestHost", guest, checkinTime);

    verify(mailSender, times(1)).send(mimeMessage);
}

@Test
void testSendCheckinSuccessMail() throws MessagingException {
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    byte[] attachment = new byte[10];
    mailService.sendCheckinSuccessMail("test@example.com", "Test Subject", "TestGuest", attachment);

    verify(mailSender, times(1)).send(mimeMessage);
}
}
