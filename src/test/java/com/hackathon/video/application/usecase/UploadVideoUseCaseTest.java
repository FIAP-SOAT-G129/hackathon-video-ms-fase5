package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseTest {

    @Mock
    private VideoRepositoryPort videoRepositoryPort;
    @Mock
    private VideoStoragePort videoStoragePort;
    @Mock
    private VideoMessagePublisherPort messagePublisherPort;

    @InjectMocks
    private UploadVideoUseCase uploadVideoUseCase;

    @Test
    void shouldUploadVideoSuccessfully() {
        // Given
        String userId = "user-123";
        String title = "My Video";
        String fileName = "video.mp4";
        InputStream content = new ByteArrayInputStream("test content".getBytes());
        String expectedPath = "/storage/video.mp4";

        when(videoStoragePort.store(any(), eq(fileName))).thenReturn(expectedPath);
        when(videoRepositoryPort.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Video result = uploadVideoUseCase.execute(userId, title, fileName, content);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(title, result.getTitle());
        assertEquals(expectedPath, result.getStoragePath());
        assertEquals(VideoStatus.PENDING, result.getStatus());
        
        verify(videoStoragePort).store(any(), eq(fileName));
        verify(videoRepositoryPort).save(any(Video.class));
        verify(messagePublisherPort).publishVideoProcessRequest(any(Video.class));
    }
}
