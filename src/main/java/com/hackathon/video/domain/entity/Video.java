package com.api.service.domain.entity;

import com.api.service.domain.enums.VideoStatus;
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
