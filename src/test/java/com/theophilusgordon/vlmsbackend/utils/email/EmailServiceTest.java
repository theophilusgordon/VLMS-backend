package com.theophilusgordon.vlmsbackend.utils.email;

import com.theophilusgordon.vlmsbackend.exception.EmailSendFailureException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO: Fix all _Failure() tests
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(emailService, "from", "test@example.com");
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:8080");
    }

    @Test
    void testSendActivationEmail_Success() {
        String recipient = "user@example.com";
        String activationCode = "123456";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendActivationEmail(recipient, activationCode);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendActivationEmail_Failure() {
        String recipient = "user@example.com";
        String activationCode = "123456";
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendActivationEmail(recipient, activationCode));
    }

    @Test
    void testSendActivatedEmail_Success() {
        String recipient = "user@example.com";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendActivatedEmail(recipient);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendActivatedEmail_Failure() {
        String recipient = "user@example.com";
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendActivatedEmail(recipient));
    }

    @Test
    void testSendRequestPasswordResetEmail_Success() {
        String recipient = "user@example.com";
        String otp = "654321";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendRequestPasswordResetEmail(recipient, otp);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendRequestPasswordResetEmail_Failure() {
        String recipient = "user@example.com";
        String otp = "654321";
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendRequestPasswordResetEmail(recipient, otp));
    }

    @Test
    void testSendPasswordResetSuccessEmail_Success() {
        String recipient = "user@example.com";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordResetSuccessEmail(recipient);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendPasswordResetSuccessEmail_Failure() {
        String recipient = "user@example.com";
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendPasswordResetSuccessEmail(recipient));
    }

    @Test
    void testSendCheckinNotificationEmail_Success() {
        String recipient = "user@example.com";
        String hostName = "Host Name";
        Guest guest = new Guest();
        guest.setPhone("123456789");
        guest.setCompany("Guest Company");
        LocalDateTime checkInTime = LocalDateTime.now();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendCheckinNotificationEmail(recipient, hostName, guest, checkInTime);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendCheckinNotificationEmail_Failure() {
        String recipient = "user@example.com";
        String hostName = "Host Name";
        Guest guest = new Guest();
        guest.setPhone("123456789");
        guest.setCompany("Guest Company");
        LocalDateTime checkInTime = LocalDateTime.now();
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendCheckinNotificationEmail(recipient, hostName, guest, checkInTime));
    }

    @Test
    void testSendCheckinSuccessEmail_Success() {
        String recipient = "user@example.com";
        String guestName = "Guest Name";
        byte[] qrCode = "fakeQRCodeData".getBytes();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendCheckinSuccessEmail(recipient, guestName, qrCode);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendCheckinSuccessEmail_Failure() {
        String recipient = "user@example.com";
        String guestName = "Guest Name";
        byte[] qrCode = "fakeQRCodeData".getBytes();
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendCheckinSuccessEmail(recipient, guestName, qrCode));
    }

    @Test
    void testSendCheckoutNotificationEmail_Success() {
        String recipient = "user@example.com";
        String hostName = "Host Name";
        Guest guest = new Guest();
        guest.setPhone("123456789");
        guest.setCompany("Guest Company");
        LocalDateTime checkoutTime = LocalDateTime.now();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendCheckoutNotificationEmail(recipient, hostName, guest, checkoutTime);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendCheckoutNotificationEmail_Failure() {
        String recipient = "user@example.com";
        String hostName = "Host Name";
        Guest guest = new Guest();
        guest.setPhone("123456789");
        guest.setCompany("Guest Company");
        LocalDateTime checkoutTime = LocalDateTime.now();
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendCheckoutNotificationEmail(recipient, hostName, guest, checkoutTime));
    }

    @Test
    void testSendCheckoutSuccessEmail_Success() {
        String recipient = "user@example.com";
        String guestName = "Guest Name";
        LocalDateTime checkoutTime = LocalDateTime.now();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Email Body");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendCheckoutSuccessEmail(recipient, guestName, checkoutTime);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendCheckoutSuccessEmail_Failure() {
        String recipient = "user@example.com";
        String guestName = "Guest Name";
        LocalDateTime checkoutTime = LocalDateTime.now();
        when(mailSender.createMimeMessage()).thenThrow(new MessagingException("Failed to create MimeMessage"));

        assertThrows(EmailSendFailureException.class, () -> emailService.sendCheckoutSuccessEmail(recipient, guestName, checkoutTime));
    }
}
