package com.hackathon.video.adapter.out.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationAdapterTest {

    @Mock private JavaMailSender mailSender;
    @InjectMocks private EmailNotificationAdapter adapter;

    @Test
    void shouldSendEmail() {
        adapter.send("user1", "Message");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldThrowMessagingExceptionWhenMailSenderFails() {
        doThrow(new RuntimeException("Mail error")).when(mailSender).send(any(SimpleMailMessage.class));
        assertThrows(com.hackathon.video.exception.MessagingException.class, () -> adapter.send("user1", "Message"));
    }
}
