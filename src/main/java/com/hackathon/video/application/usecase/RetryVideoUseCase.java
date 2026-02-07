package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.BusinessException;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetryVideoUseCase {
    private final VideoRepositoryPort repository;
    private final VideoMessagePublisherPort publisher;

    public Video execute(UUID videoId) {
        Video video = repository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));

        if (video.getStatus() != VideoStatus.ERROR) {
            throw new BusinessException("Only videos with ERROR status can be retried");
        }

        video.setStatus(VideoStatus.PENDING);
        video.setErrorMessage(null);
        Video savedVideo = repository.save(video);

        publisher.publishVideoProcessRequest(savedVideo);
        return savedVideo;
    }
}
