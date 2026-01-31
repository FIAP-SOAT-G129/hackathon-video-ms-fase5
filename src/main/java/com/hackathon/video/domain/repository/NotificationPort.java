package com.api.service.domain.repository;

public interface NotificationPort {
    void send(String userId, String message);
}
