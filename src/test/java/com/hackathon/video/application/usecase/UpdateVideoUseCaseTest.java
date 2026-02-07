package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @InjectMocks private UpdateVideoUseCase useCase;

    @Test
    void shouldUpdateTitle() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).userId("user1").title("Old").originalFileName("file.mp4").build();
        when(repository.findById(id)).thenReturn(Optional.of(video));
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Video result = useCase.execute(id, "New Title");

        assertEquals("New Title", result.getTitle());
    }
}
