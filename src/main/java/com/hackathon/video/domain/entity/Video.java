package com.hackathon.video.domain.entity;

import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private UUID id;
    private String userId;
    private String title;
    private String originalFileName;
    private String storagePath;
    private String zipResultPath;
    private VideoStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void validate() {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException("User ID is required");
        }
        if (title == null || title.isBlank()) {
            throw new BusinessException("Title is required");
        }
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new BusinessException("Original file name is required");
        }
    }

    public void updateStatus(VideoStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsError(String errorMessage) {
        this.status = VideoStatus.ERROR;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
}
