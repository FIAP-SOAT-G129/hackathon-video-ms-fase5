package com.api.service.domain.repository;

import com.api.service.domain.entity.Video;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepositoryPort {
    Video save(Video video);
    Optional<Video> findById(UUID id);
    List<Video> findByUserId(String userId);
    void delete(UUID id);
}
