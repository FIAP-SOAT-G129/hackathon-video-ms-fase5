// java
package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateVideoStatusUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private NotificationPort notificationPort;
    @InjectMocks private UpdateVideoStatusUseCase useCase;

    @Test
    void shouldUpdateStatus() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).status(VideoStatus.PENDING).build();
        when(repository.findById(id)).thenReturn(Optional.of(video));

        useCase.execute(id, VideoStatus.DONE, null);

        verify(repository).save(argThat(v -> v.getStatus() == VideoStatus.DONE));
        verify(notificationPort).send(isNull(), eq("Seu vídeo null foi processado com sucesso!"));
    }
}
