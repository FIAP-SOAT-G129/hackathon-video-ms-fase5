package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.adapter.in.dto.FileDownloadResultDTO;
import com.hackathon.video.adapter.in.dto.VideoResponseDTO;
import com.hackathon.video.adapter.out.mapper.VideoMapper;
import com.hackathon.video.application.usecase.*;
import com.hackathon.video.domain.entity.Video;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final UploadVideoUseCase uploadVideoUseCase;
    private final GetVideoUseCase getVideoUseCase;
    private final DownloadVideoUseCase downloadVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final RetryVideoUseCase retryVideoUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;

    @PostMapping(
            path = "/user/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<VideoResponseDTO> upload(
            @PathVariable String userId,
            @RequestPart("title") String title,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Video video = uploadVideoUseCase.execute(userId, title, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(VideoMapper.toDTO(video));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoResponseDTO>> list(@PathVariable String userId) {
        List<Video> videos = getVideoUseCase.findByUserId(userId);
        return ResponseEntity.ok(videos.stream().map(VideoMapper::toDTO).toList());
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoResponseDTO> getById(@PathVariable UUID videoId) {
        Video video = getVideoUseCase.findById(videoId);
        return ResponseEntity.ok((VideoMapper.toDTO(video)));
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoResponseDTO> update(
            @PathVariable UUID videoId,
            @RequestPart("title") String title,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Video video = updateVideoUseCase.execute(videoId, title, file);
        return ResponseEntity.ok(VideoMapper.toDTO(video));
    }

    @PostMapping("/{videoId}/retry")
    public ResponseEntity<VideoResponseDTO> retry(@PathVariable UUID videoId) {
        Video video = retryVideoUseCase.execute(videoId);
        return ResponseEntity.ok(VideoMapper.toDTO(video));
    }

    @GetMapping("/{videoId}/download")
    public ResponseEntity<Resource> downloadVideo(@PathVariable UUID videoId) {
        FileDownloadResultDTO result = downloadVideoUseCase.downloadVideo(videoId);

        InputStreamResource resource = new InputStreamResource(result.getInputStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{videoId}/zip")
    public ResponseEntity<Resource> downloadZip(@PathVariable UUID videoId) {
        FileDownloadResultDTO result = downloadVideoUseCase.downloadZip(videoId);

        InputStreamResource resource = new InputStreamResource(result.getInputStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> delete(@PathVariable UUID videoId) {
        deleteVideoUseCase.execute(videoId);
        return ResponseEntity.noContent().build();
    }
}
