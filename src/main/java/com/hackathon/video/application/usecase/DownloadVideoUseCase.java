package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.BusinessException;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DownloadVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;

    public InputStream downloadOriginal(UUID videoId) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));
        return videoStoragePort.retrieve(video.getStoragePath());
    }

    public InputStream downloadZip(UUID videoId) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));
        
        if (video.getZipResultPath() == null) {
            throw new BusinessException("Zip result not available for video id: " + videoId);
        }
        
        return videoStoragePort.retrieve(video.getZipResultPath());
    }
}
