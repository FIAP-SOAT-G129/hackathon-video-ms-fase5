package com.hackathon.video.domain.repository;

import com.hackathon.video.domain.entity.Video;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepositoryPort {
    Video save(Video video);
    Optional<Video> findById(UUID id);
    List<Video> findByUserId(String userId);
    void delete(UUID id);
}
