package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private VideoStoragePort storage;
    @Mock private VideoMessagePublisherPort publisher;

    @InjectMocks private UploadVideoUseCase useCase;

    @Test
    void shouldUploadVideoSuccessfully() {
        when(storage.store(any(), anyString())).thenReturn("/path/video.mp4");
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Video result = useCase.execute("user1", "Title", "video.mp4", mock(InputStream.class));

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        verify(publisher).publishVideoProcessRequest(any());
    }
}
