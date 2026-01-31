package com.hackathon.video.adapter.out.entity;

import com.hackathon.video.domain.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "videos")
@Data
public class VideoEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storagePath;

    private String zipResultPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus status;

    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
