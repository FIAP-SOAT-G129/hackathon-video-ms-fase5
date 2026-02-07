package com.hackathon.video.application.usecase;

import com.hackathon.video.adapter.in.dto.FileDownloadResultDTO;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.BusinessException;
import com.hackathon.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DownloadVideoUseCase {

    private final VideoRepositoryPort videoRepositoryPort;
    private final VideoStoragePort videoStoragePort;

    public FileDownloadResultDTO downloadVideo(UUID videoId) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));

        InputStream is = videoStoragePort.retrieve(StorageType.VIDEO, video.getStoragePath());

        return FileDownloadResultDTO.builder()
                .fileName(video.getFileName())
                .extension(video.getExtension())
                .mimeType(video.getMimeType())
                .inputStream(is)
                .build();
    }

    public FileDownloadResultDTO downloadZip(UUID videoId) {
        Video video = videoRepositoryPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + videoId));

        if (video.getStatus().equals(VideoStatus.ERROR)) {
            throw new BusinessException("Video processing failed for video id: " + videoId + ". Error: " + video.getErrorMessage());
        } else if (!video.getStatus().equals(VideoStatus.DONE)) {
            throw new BusinessException("Video is still being processed for video id: " + videoId);
        }

        if (video.getZipPath() == null) {
            throw new BusinessException("Zip file is not available for video id: " + videoId);
        }

        InputStream is = videoStoragePort.retrieve(StorageType.ZIP, video.getZipPath());

        return FileDownloadResultDTO.builder()
                .fileName(video.getTitle() + ".zip")
                .extension(".zip")
                .mimeType("application/zip")
                .inputStream(is)
                .build();
    }
}
