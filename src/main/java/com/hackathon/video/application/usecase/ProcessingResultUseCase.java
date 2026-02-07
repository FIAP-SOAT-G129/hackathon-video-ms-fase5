package com.hackathon.video.application.usecase;

import com.hackathon.video.adapter.in.dto.ProcessingRequestDTO;
import com.hackathon.video.application.dto.NotificationEvent;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.NotificationPublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessingResultUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final NotificationPublisherPort notificationPublisherPort;

    public void execute(UUID videoId, ProcessingRequestDTO request) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));

        video.setStatus(request.getStatus());

        if (request.getStatus() == VideoStatus.DONE) {
            video.setZipPath(request.getZipPath());
        } else if (request.getStatus() == VideoStatus.ERROR) {
            video.markAsError(request.getErrorMessage());
        }

        videoRepositoryPort.save(video);
        
        String message = "";
        if (request.getStatus() == VideoStatus.DONE) {
            message = "Seu vídeo " + video.getTitle() + " foi processado com sucesso e já está disponível para download!";
        } else if (request.getStatus() == VideoStatus.ERROR) {
            message = "Erro ao processar vídeo " + video.getTitle() + ": " + request.getErrorMessage();
        }

        if (!message.isEmpty()) {
            notificationPublisherPort.publish(NotificationEvent.builder()
                    .userId(video.getUserId())
                    .message(message)
                    .build());
        }
    }
}
