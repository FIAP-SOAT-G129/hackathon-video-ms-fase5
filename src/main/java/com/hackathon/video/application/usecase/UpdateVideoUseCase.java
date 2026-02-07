package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateVideoUseCase {
    private final VideoRepositoryPort repository;

    public Video execute(UUID videoId, String title) {
        Video video = repository.findById(videoId)
                .orElseThrow(() -> new BusinessException("Video not found"));
        
        video.setTitle(title);
        video.validate();
        return repository.save(video);
    }
}
