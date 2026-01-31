package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;

    public Video findById(UUID id) {
        return videoRepositoryPort.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));
    }

    public List<Video> findByUserId(String userId) {
        return videoRepositoryPort.findByUserId(userId);
    }
}
