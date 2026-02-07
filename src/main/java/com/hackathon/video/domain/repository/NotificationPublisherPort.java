package com.hackathon.video.domain.repository;

import com.hackathon.video.application.dto.NotificationEvent;

public interface NotificationPublisherPort {
    void publish(NotificationEvent event);
}
