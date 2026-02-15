package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private VideoStoragePort storage;
    @Mock private VideoMessagePublisherPort publisher;

    @InjectMocks
    private UpdateVideoUseCase useCase;

    @Test
    void shouldUpdateVideoSuccessfully() throws IOException {
        UUID videoId = UUID.randomUUID();
        String newTitle = "New Title";
        String fileName = "video.mp4";
        String contentType = "video/mp4";
        String newPath = "/storage/video.mp4";

        Video existingVideo = Video.builder().id(videoId).storagePath("/old/path.mp4").mimeType(contentType).build();

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getContentType()).thenReturn(contentType);
        when(file.getInputStream()).thenReturn(mock(InputStream.class));

        when(repository.findById(videoId)).thenReturn(Optional.of(existingVideo));
        when(storage.store(eq(videoId), any(InputStream.class), any())).thenReturn(newPath);
        when(repository.save(any(Video.class))).thenAnswer(i -> i.getArguments()[0]);

        Video result = useCase.execute(videoId, newTitle, file);

        assertNotNull(result);
        assertEquals(newTitle, result.getTitle());
        assertEquals(VideoStatus.PENDING, result.getStatus());
        assertEquals(newPath, result.getStoragePath());

        verify(storage).delete(eq(StorageType.VIDEO), anyString());
        verify(storage).store(eq(videoId), any(), any());
        verify(repository).save(any(Video.class));
        verify(publisher).publishVideoProcessRequest(result);
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        assertThrows(com.hackathon.video.exception.BusinessException.class, () -> useCase.execute(UUID.randomUUID(), "t", file));
    }

    @Test
    void shouldThrowExceptionWhenFormatUnsupported() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        assertThrows(com.hackathon.video.exception.BusinessException.class, () -> useCase.execute(UUID.randomUUID(), "t", file));
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFound() {
        UUID id = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("video/mp4");
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(com.hackathon.video.exception.VideoNotFoundException.class, () -> useCase.execute(id, "t", file));
    }
}