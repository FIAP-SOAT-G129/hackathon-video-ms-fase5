package com.api.service.domain.repository;

import com.api.service.domain.entity.Video;

public interface VideoMessagePublisherPort {
    void publishVideoProcessRequest(Video video);
}
