package com.hackathon.video.domain.entity;

import com.hackathon.video.domain.enums.SupportedVideoFormat;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.exception.BusinessException;
import lombok.*;

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
    private String fileName;
    @Setter(AccessLevel.NONE)
    private String mimeType;
    private String storagePath;
    private String zipPath;
    private VideoStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class VideoBuilder {
        public VideoBuilder mimeType(String mimeType) {
            this.mimeType = SupportedVideoFormat.fromMimeType(mimeType).getMimeType();
            return this;
        }
    }

    public void validate() {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException("User ID is required");
        }
        if (title == null || title.isBlank()) {
            throw new BusinessException("Title is required");
        }
        if (fileName == null || fileName.isBlank()) {
            throw new BusinessException("File name is required");
        }

        SupportedVideoFormat.fromMimeType(this.mimeType);
    }

    public void setMimeType(String mimeType) {
        this.mimeType = SupportedVideoFormat.fromMimeType(mimeType).getMimeType();
    }

    public void setStoragePath(String storagePath) {
        SupportedVideoFormat.fromMimeType(this.mimeType);

        this.storagePath = storagePath;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(VideoStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsError(String errorMessage) {
        this.status = VideoStatus.ERROR;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }

    public String getExtension() {
        return SupportedVideoFormat.fromMimeType(this.mimeType).getExtension();
    }
}
