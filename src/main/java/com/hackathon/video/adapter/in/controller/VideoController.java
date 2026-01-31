package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.adapter.in.dto.UpdateStatusRequestDTO;
import com.hackathon.video.adapter.in.dto.VideoResponseDTO;
import com.hackathon.video.application.usecase.*;
import com.hackathon.video.domain.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final UploadVideoUseCase uploadVideoUseCase;
    private final GetVideoUseCase getVideoUseCase;
    private final UpdateVideoStatusUseCase updateVideoStatusUseCase;
    private final DownloadVideoUseCase downloadVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    @PostMapping
    public ResponseEntity<VideoResponseDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestHeader("X-User-Id") String userId) throws IOException {
        
        Video video = uploadVideoUseCase.execute(userId, title, file.getOriginalFilename(), file.getInputStream());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(video));
    }

    @GetMapping
    public ResponseEntity<List<VideoResponseDTO>> list(@RequestHeader("X-User-Id") String userId) {
        List<Video> videos = getVideoUseCase.findByUserId(userId);
        return ResponseEntity.ok(videos.stream().map(this::mapToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoResponseDTO> getById(@PathVariable UUID videoId) {
        Video video = getVideoUseCase.findById(videoId);
        return ResponseEntity.ok(mapToDTO(video));
    }

    @PatchMapping("/{videoId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID videoId,
            @RequestBody UpdateStatusRequestDTO request) {
        updateVideoStatusUseCase.execute(videoId, request.getStatus(), request.getErrorMessage());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{videoId}/download")
    public ResponseEntity<byte[]> downloadOriginal(@PathVariable UUID videoId) throws IOException {
        InputStream is = downloadVideoUseCase.downloadOriginal(videoId);
        return ResponseEntity.ok(is.readAllBytes());
    }

    @GetMapping("/{videoId}/zip")
    public ResponseEntity<byte[]> downloadZip(@PathVariable UUID videoId) throws IOException {
        InputStream is = downloadVideoUseCase.downloadZip(videoId);
        return ResponseEntity.ok(is.readAllBytes());
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> delete(@PathVariable UUID videoId) {
        deleteVideoUseCase.execute(videoId);
        return ResponseEntity.noContent().build();
    }

    private VideoResponseDTO mapToDTO(Video video) {
        VideoResponseDTO dto = new VideoResponseDTO();
        dto.setId(video.getId());
        dto.setUserId(video.getUserId());
        dto.setTitle(video.getTitle());
        dto.setOriginalFileName(video.getOriginalFileName());
        dto.setStatus(video.getStatus());
        dto.setErrorMessage(video.getErrorMessage());
        dto.setCreatedAt(video.getCreatedAt());
        return dto;
    }
}
