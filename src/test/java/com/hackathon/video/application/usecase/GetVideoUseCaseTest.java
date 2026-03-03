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

    @Test
    void shouldFindByUserId() {
        String userId = "user1";

        Video video1 = Video.builder().id(UUID.randomUUID()).build();
        Video video2 = Video.builder().id(UUID.randomUUID()).build();

        when(repository.findByUserId(userId))
                .thenReturn(java.util.List.of(video1, video2));

        var result = useCase.findByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(video1, result.get(0));
        assertEquals(video2, result.get(1));

        verify(repository).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoVideos() {
        String userId = "user2";

        when(repository.findByUserId(userId))
                .thenReturn(java.util.Collections.emptyList());

        var result = useCase.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository).findByUserId(userId);
    }
}
