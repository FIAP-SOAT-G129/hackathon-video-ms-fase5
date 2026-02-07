package com.hackathon.video.adapter.out.messaging;

import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.config.RabbitMQConfig;
import com.hackathon.video.domain.repository.NotificationPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQNotificationPublisherAdapter implements NotificationPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(NotificationEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                event
        );
    }
}
