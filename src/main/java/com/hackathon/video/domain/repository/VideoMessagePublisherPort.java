package com.hackathon.video.domain.repository;

import com.hackathon.video.domain.entity.Video;

public interface VideoMessagePublisherPort {
    void publishVideoProcessRequest(Video video);
}
