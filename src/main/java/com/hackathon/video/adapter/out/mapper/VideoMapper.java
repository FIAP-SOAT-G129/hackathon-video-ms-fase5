package com.hackathon.video.adapter.out.mapper;

import com.hackathon.video.adapter.in.dto.VideoResponseDTO;
import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.domain.entity.Video;

public class VideoMapper {

   private VideoMapper() {}

   public static Video toDomain(VideoEntity entity) {
        if (entity == null) return null;
        
        return Video.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .fileName(entity.getFileName())
                .mimeType(entity.getMimeType())
                .storagePath(entity.getStoragePath())
                .zipPath(entity.getZipPath())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static VideoEntity toEntity(Video domain) {
        if (domain == null) return null;

        VideoEntity entity = new VideoEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setTitle(domain.getTitle());
        entity.setFileName(domain.getFileName());
        entity.setMimeType(domain.getMimeType());
        entity.setStoragePath(domain.getStoragePath());
        entity.setZipPath(domain.getZipPath());
        entity.setStatus(domain.getStatus());
        entity.setErrorMessage(domain.getErrorMessage());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static VideoResponseDTO toDTO(Video video) {
        VideoResponseDTO dto = new VideoResponseDTO();
        dto.setId(video.getId());
        dto.setUserId(video.getUserId());
        dto.setTitle(video.getTitle());
        dto.setOriginalFileName(video.getFileName());
        dto.setStatus(video.getStatus());
        dto.setErrorMessage(video.getErrorMessage());
        dto.setCreatedAt(video.getCreatedAt());
        return dto;
    }
}
