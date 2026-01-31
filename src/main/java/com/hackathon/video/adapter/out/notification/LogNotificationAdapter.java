package com.hackathon.video.adapter.out.notification;

import com.hackathon.video.domain.repository.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class LogNotificationAdapter implements NotificationPort {
    @Override
    public void send(String userId, String message) {
        System.out.println("[NOTIFICATION] To User: " + userId + " | Message: " + message);
    }
}
