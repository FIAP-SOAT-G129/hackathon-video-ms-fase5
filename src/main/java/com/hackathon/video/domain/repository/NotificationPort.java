package com.hackathon.video.domain.repository;

public interface NotificationPort {
    void send(String email, String message);
}
