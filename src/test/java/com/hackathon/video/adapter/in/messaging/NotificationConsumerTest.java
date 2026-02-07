package com.hackathon.video.adapter.in.messaging;

import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.domain.repository.NotificationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    void shouldConsumeAndSendNotification() {
        NotificationEvent event = NotificationEvent.builder()
                .userId("user123")
                .message("Test message")
                .build();

        notificationConsumer.consume(event);

        verify(notificationPort).send("user123", "Test message");
    }
}
