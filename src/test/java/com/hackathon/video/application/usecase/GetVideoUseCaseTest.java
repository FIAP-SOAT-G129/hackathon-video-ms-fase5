package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.VideoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @InjectMocks private GetVideoUseCase useCase;

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).build();
        when(repository.findById(id)).thenReturn(Optional.of(video));

        Video result = useCase.findById(id);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VideoNotFoundException.class, () -> useCase.findById(id));
    }
}
