package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;
    private final VideoMessagePublisherPort messagePublisherPort;

    public Video execute(String userId, String title, String fileName, InputStream content) {
        String storagePath = videoStoragePort.store(content, fileName);

        Video video = Video.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .title(title)
                .originalFileName(fileName)
                .storagePath(storagePath)
                .status(VideoStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Video savedVideo = videoRepositoryPort.save(video);
        messagePublisherPort.publishVideoProcessRequest(savedVideo);

        return savedVideo;
    }
}
