package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.SupportedVideoFormat;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.BusinessException;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;
    private final VideoMessagePublisherPort messagePublisherPort;

    public Video execute(String userId, String title, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File must not be empty");
        }

        if (!SupportedVideoFormat.isSupported(file.getContentType())) {
            throw new BusinessException("Unsupported video format: " + file.getContentType());
        }

        Video video = Video.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .title(title)
                .fileName(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .status(VideoStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String storagePath = videoStoragePort.store(video.getId(), file.getInputStream(), video.getExtension());

        video.setStoragePath(storagePath);

        Video savedVideo = videoRepositoryPort.save(video);
        messagePublisherPort.publishVideoProcessRequest(savedVideo);

        return savedVideo;
    }
}
