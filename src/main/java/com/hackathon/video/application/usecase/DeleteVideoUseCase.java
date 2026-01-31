package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;

    public void execute(UUID videoId) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));
        
        if (video.getStoragePath() != null) videoStoragePort.delete(video.getStoragePath());
        if (video.getZipResultPath() != null) videoStoragePort.delete(video.getZipResultPath());

        // Remove do banco
        videoRepositoryPort.delete(videoId);
    }
}
