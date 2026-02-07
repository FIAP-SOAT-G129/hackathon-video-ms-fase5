package com.hackathon.video.adapter.in.messaging;

import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.repository.UserIdentityPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private UserIdentityPort userIdentityPort;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    void shouldResolveEmailAndSendNotification() {
        NotificationEvent event = NotificationEvent.builder()
                .userId("user123")
                .message("Test message")
                .build();

        when(userIdentityPort.getEmailByUserId("user123")).thenReturn(Optional.of("user123@example.com"));

        notificationConsumer.consume(event);

        verify(userIdentityPort).getEmailByUserId("user123");
        verify(notificationPort).send("user123@example.com", "Test message");
    }

    @Test
    void shouldSendNotificationDirectlyIfEmailIsProvided() {
        NotificationEvent event = NotificationEvent.builder()
                .userId("user123")
                .email("direct@example.com")
                .message("Test message")
                .build();

        notificationConsumer.consume(event);

        verifyNoInteractions(userIdentityPort);
        verify(notificationPort).send("direct@example.com", "Test message");
    }
}
