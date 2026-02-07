package com.hackathon.video.adapter.out.messaging;

import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQNotificationPublisherAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQNotificationPublisherAdapter publisherAdapter;

    @Test
    void shouldPublishNotificationEvent() {
        NotificationEvent event = NotificationEvent.builder()
                .userId("user123")
                .message("Test message")
                .build();

        publisherAdapter.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.NOTIFICATION_EXCHANGE),
                eq(RabbitMQConfig.NOTIFICATION_ROUTING_KEY),
                eq(event)
        );
    }
}
