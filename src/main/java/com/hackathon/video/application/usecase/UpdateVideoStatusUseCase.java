package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateVideoStatusUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final NotificationPort notificationPort;

    public void execute(UUID videoId, VideoStatus status, String errorMessage) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        if (status == VideoStatus.ERROR) {
            video.markAsError(errorMessage);
        } else {
            video.setStatus(status);
        }

        videoRepositoryPort.save(video);
        
        if (status == VideoStatus.DONE) {
            notificationPort.send(video.getUserId(), "Seu vídeo " + video.getTitle() + " foi processado com sucesso!");
        } else if (status == VideoStatus.ERROR) {
            notificationPort.send(video.getUserId(), "Erro ao processar vídeo " + video.getTitle() + ": " + errorMessage);
        }
    }
}
