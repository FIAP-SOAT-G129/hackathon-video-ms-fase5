package com.hackathon.video.adapter.in.messaging;

import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.config.RabbitMQConfig;
import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.repository.UserIdentityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationPort notificationPort;
    private final UserIdentityPort userIdentityPort;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void consume(NotificationEvent event) {
        log.info("Consuming notification event for user: {}", event.getUserId());
        
        try {
            String email = event.getEmail();
            if (email == null || email.isEmpty()) {
                Optional<String> resolvedEmail = userIdentityPort.getEmailByUserId(event.getUserId());
                if (resolvedEmail.isPresent()) {
                    email = resolvedEmail.get();
                } else {
                    log.error("Could not resolve email for user: {}. Notification aborted.", event.getUserId());
                    return;
                }
            }

            notificationPort.send(email, event.getMessage());
            log.info("Notification sent successfully to: {}", email);
            
        } catch (Exception e) {
            log.error("Failed to process notification for user: {}", event.getUserId(), e);
            throw e;
        }
    }
}
