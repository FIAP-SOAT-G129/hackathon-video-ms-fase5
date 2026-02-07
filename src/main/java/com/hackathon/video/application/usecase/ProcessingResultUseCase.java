package com.hackathon.video.application.usecase;

import com.hackathon.video.adapter.in.dto.ProcessingRequestDTO;
import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessingResultUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final NotificationPort notificationPort;

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
        
        if (request.getStatus() == VideoStatus.DONE) {
            notificationPort.send(video.getUserId(), "Seu vídeo " + video.getTitle() + " foi processado com sucesso e já está disponível para download!");
        } else if (request.getStatus() == VideoStatus.ERROR) {
            notificationPort.send(video.getUserId(), "Erro ao processar vídeo " + video.getTitle() + ": " + request.getErrorMessage());
        }
    }
}
