package com.hackathon.video.domain.repository;

public interface NotificationPort {
    void send(String userId, String message);
}
