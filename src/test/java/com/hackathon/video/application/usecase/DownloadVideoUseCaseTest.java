package com.hackathon.video.application.usecase;

import com.hackathon.video.adapter.in.dto.FileDownloadResultDTO;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.BusinessException;
import com.hackathon.video.exception.VideoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadVideoUseCaseTest {

    @Mock
    private VideoRepositoryPort videoRepositoryPort;

    @Mock
    private VideoStoragePort videoStoragePort;

    @InjectMocks
    private DownloadVideoUseCase useCase;

    @Test
    void shouldDownloadOriginalVideoSuccessfully() {
        UUID videoId = UUID.randomUUID();
        InputStream inputStream = new ByteArrayInputStream("video-content".getBytes());

        Video video = Video.builder()
                .id(videoId)
                .fileName("my-video.mp4")
                .mimeType("video/mp4")
                .storagePath("videos/my-video.mp4")
                .status(VideoStatus.DONE)
                .build();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.of(video));
        when(videoStoragePort.retrieve(StorageType.VIDEO, "videos/my-video.mp4"))
                .thenReturn(inputStream);

        FileDownloadResultDTO result = useCase.downloadVideo(videoId);

        assertNotNull(result);
        assertEquals("my-video.mp4", result.getFileName());
        assertEquals(".mp4", result.getExtension());
        assertEquals("video/mp4", result.getMimeType());
        assertEquals(inputStream, result.getInputStream());

        verify(videoStoragePort).retrieve(StorageType.VIDEO, "videos/my-video.mp4");
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFoundOnDownloadVideo() {
        UUID videoId = UUID.randomUUID();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.empty());

        assertThrows(
                VideoNotFoundException.class,
                () -> useCase.downloadVideo(videoId)
        );

        verify(videoStoragePort, never()).retrieve(any(), any());
    }

    @Test
    void shouldDownloadZipSuccessfullyWhenVideoIsDone() {
        UUID videoId = UUID.randomUUID();
        InputStream inputStream = new ByteArrayInputStream("zip-content".getBytes());

        Video video = Video.builder()
                .id(videoId)
                .title("my-video")
                .status(VideoStatus.DONE)
                .zipPath("zips/my-video.zip")
                .build();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.of(video));
        when(videoStoragePort.retrieve(StorageType.ZIP, "zips/my-video.zip"))
                .thenReturn(inputStream);

        FileDownloadResultDTO result = useCase.downloadZip(videoId);

        assertNotNull(result);
        assertEquals("my-video.zip", result.getFileName());
        assertEquals(".zip", result.getExtension());
        assertEquals("application/zip", result.getMimeType());
        assertEquals(inputStream, result.getInputStream());

        verify(videoStoragePort).retrieve(StorageType.ZIP, "zips/my-video.zip");
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNotDoneOnZipDownload() {
        UUID videoId = UUID.randomUUID();

        Video video = Video.builder()
                .id(videoId)
                .status(VideoStatus.PROCESSING)
                .build();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.of(video));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.downloadZip(videoId)
        );

        assertEquals(
                "Video is still being processed for video id: " + videoId,
                exception.getMessage()
        );

        verify(videoStoragePort, never()).retrieve(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenZipPathIsNull() {
        UUID videoId = UUID.randomUUID();

        Video video = Video.builder()
                .id(videoId)
                .status(VideoStatus.DONE)
                .zipPath(null)
                .build();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.of(video));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.downloadZip(videoId)
        );

        assertEquals(
                "Zip file is not available for video id: " + videoId,
                exception.getMessage()
        );

        verify(videoStoragePort, never()).retrieve(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFoundOnZipDownload() {
        UUID videoId = UUID.randomUUID();

        when(videoRepositoryPort.findById(videoId)).thenReturn(Optional.empty());

        assertThrows(
                VideoNotFoundException.class,
                () -> useCase.downloadZip(videoId)
        );

        verify(videoStoragePort, never()).retrieve(any(), any());
    }
}
