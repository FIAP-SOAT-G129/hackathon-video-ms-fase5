package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    void shouldUploadVideoSuccessfully() throws IOException {
        when(storage.store(any(), any(), anyString())).thenReturn("/path/video.mp4");
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("video.mp4");
        when(file.getContentType()).thenReturn("video/mp4");
        when(file.getInputStream()).thenReturn(mock(InputStream.class));

        Video result = useCase.execute("user1", "Title", file);

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        verify(publisher).publishVideoProcessRequest(any());
    }
}
