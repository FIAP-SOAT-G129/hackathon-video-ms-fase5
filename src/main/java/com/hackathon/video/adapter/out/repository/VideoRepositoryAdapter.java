package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoRepositoryAdapter implements VideoRepositoryPort {

    private final JpaVideoRepository jpaVideoRepository;

    @Override
    public Video save(Video video) {
        VideoEntity entity = mapToEntity(video);
        VideoEntity savedEntity = jpaVideoRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Video> findById(UUID id) {
        return jpaVideoRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Video> findByUserId(String userId) {
        return jpaVideoRepository.findByUserId(userId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaVideoRepository.deleteById(id);
    }

    private VideoEntity mapToEntity(Video video) {
        VideoEntity entity = new VideoEntity();
        entity.setId(video.getId());
        entity.setUserId(video.getUserId());
        entity.setTitle(video.getTitle());
        entity.setOriginalFileName(video.getOriginalFileName());
        entity.setStoragePath(video.getStoragePath());
        entity.setZipResultPath(video.getZipResultPath());
        entity.setStatus(video.getStatus());
        entity.setErrorMessage(video.getErrorMessage());
        entity.setCreatedAt(video.getCreatedAt());
        entity.setUpdatedAt(video.getUpdatedAt());
        return entity;
    }

    private Video mapToDomain(VideoEntity entity) {
        return Video.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .originalFileName(entity.getOriginalFileName())
                .storagePath(entity.getStoragePath())
                .zipResultPath(entity.getZipResultPath())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
