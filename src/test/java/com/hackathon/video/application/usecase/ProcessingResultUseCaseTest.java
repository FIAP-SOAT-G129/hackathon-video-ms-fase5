package com.hackathon.video.application.usecase;

import com.hackathon.video.adapter.in.dto.ProcessingRequestDTO;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.repository.UserIdentityPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.VideoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessingResultUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private NotificationPort notificationPort;
    @Mock private UserIdentityPort userIdentityPort;
    @InjectMocks private ProcessingResultUseCase useCase;

    @Test
    void shouldUpdateVideoAndNotifyUserWhenStatusIsDone() {
        UUID id = UUID.randomUUID();
        String email = "user-1@example.com";

        ProcessingRequestDTO request = ProcessingRequestDTO.builder()
                .status(VideoStatus.DONE)
                .zipPath("/path/to/zip/my-video.zip")
                .build();

        Video video = Video.builder()
                .id(id)
                .userId("user-1")
                .title("my-video")
                .status(VideoStatus.PENDING)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(video));
        when(userIdentityPort.getEmailByUserId("user-1")).thenReturn(Optional.of(email));

        useCase.execute(id, request);

        verify(repository).save(argThat(v ->
                v.getStatus() == VideoStatus.DONE &&
                        "/path/to/zip/my-video.zip".equals(v.getZipPath())
        ));

        verify(notificationPort).send(
                eq(email),
                eq("Seu vídeo " + video.getTitle() + " foi processado com sucesso e já está disponível para download!")
        );
    }

    @Test
    void shouldMarkVideoAsErrorAndNotifyUserWhenStatusIsError() {
        UUID id = UUID.randomUUID();
        String email = "user-1@example.com";

        ProcessingRequestDTO request = ProcessingRequestDTO.builder()
                .status(VideoStatus.ERROR)
                .errorMessage("FFmpeg failed")
                .build();

        Video video = Video.builder()
                .id(id)
                .userId("user-1")
                .title("my-video")
                .status(VideoStatus.PROCESSING)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(video));
        when(userIdentityPort.getEmailByUserId("user-1")).thenReturn(Optional.of(email));

        useCase.execute(id, request);

        verify(repository).save(argThat(v -> v.getStatus() == VideoStatus.ERROR && "FFmpeg failed".equals(v.getErrorMessage())));

        verify(notificationPort).send(
                eq(email),
                eq("Erro ao processar vídeo my-video: FFmpeg failed")
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFound() {
        UUID id = UUID.randomUUID();

        ProcessingRequestDTO request = ProcessingRequestDTO.builder()
                .status(VideoStatus.DONE)
                .zipPath("/path/to/zip.zip")
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> useCase.execute(id, request));

        verify(repository, never()).save(any());
        verify(notificationPort, never()).send(any(), any());
    }
}
