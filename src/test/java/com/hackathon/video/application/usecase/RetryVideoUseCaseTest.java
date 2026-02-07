package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.exception.BusinessException;
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
class RetryVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private VideoMessagePublisherPort publisher;
    @InjectMocks private RetryVideoUseCase useCase;

    @Test
    void shouldRetryVideoInErrorStatus() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).status(VideoStatus.ERROR).build();
        when(repository.findById(id)).thenReturn(Optional.of(video));
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Video result = useCase.execute(id);

        assertEquals(VideoStatus.PENDING, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(publisher).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenNotErrorStatus() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).status(VideoStatus.COMPLETED).build();
        when(repository.findById(id)).thenReturn(Optional.of(video));

        assertThrows(BusinessException.class, () -> useCase.execute(id));
    }
}
