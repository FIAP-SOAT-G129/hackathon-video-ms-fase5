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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;
    private final VideoMessagePublisherPort messagePublisherPort;

    public Video execute(UUID videoId, String title, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File must not be empty");
        }

        if (!SupportedVideoFormat.isSupported(file.getContentType())) {
            throw new BusinessException("Unsupported video format: " + file.getContentType());
        }

        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));

        video.setTitle(title);
        video.setFileName(file.getOriginalFilename());
        video.setMimeType(file.getContentType());
        video.setStatus(VideoStatus.PENDING);

        if (video.getStoragePath() != null) videoStoragePort.delete(StorageType.VIDEO, video.getStoragePath());

        String storagePath = videoStoragePort.store(video.getId(), file.getInputStream(), video.getExtension());
        video.setStoragePath(storagePath);

        Video savedVideo = videoRepositoryPort.save(video);
        messagePublisherPort.publishVideoProcessRequest(savedVideo);

        return savedVideo;
    }
}
