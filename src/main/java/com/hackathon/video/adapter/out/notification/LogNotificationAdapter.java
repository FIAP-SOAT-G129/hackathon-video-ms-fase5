package com.hackathon.video.adapter.out.notification;

import com.hackathon.video.domain.repository.NotificationPort;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LogNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LogNotificationAdapter.class);

    @Override
    public void send(String userId, String message) {
        log.info("[NOTIFICATION] To User: {} | Message: {}", userId, message);
    }
}
