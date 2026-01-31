package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.adapter.in.dto.UpdateStatusRequestDTO;
import com.hackathon.video.adapter.in.dto.VideoResponseDTO;
import com.hackathon.video.adapter.out.mapper.VideoMapper;
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

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final UploadVideoUseCase uploadVideoUseCase;
    private final GetVideoUseCase getVideoUseCase;
    private final UpdateVideoStatusUseCase updateVideoStatusUseCase;
    private final DownloadVideoUseCase downloadVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    @PostMapping("/user/{userId}")
    public ResponseEntity<VideoResponseDTO> upload(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) throws IOException {
        
        Video video = uploadVideoUseCase.execute(userId, title, file.getOriginalFilename(), file.getInputStream());
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
}
