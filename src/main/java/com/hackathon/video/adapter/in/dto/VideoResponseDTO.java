package com.hackathon.video.adapter.in.dto;

import com.hackathon.video.domain.enums.VideoStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VideoResponseDTO {
    private UUID id;
    private String userId;
    private String title;
    private String originalFileName;
    private VideoStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
}
