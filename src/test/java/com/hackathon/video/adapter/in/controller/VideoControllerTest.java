package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.adapter.in.dto.FileDownloadResultDTO;
import com.hackathon.video.application.usecase.*;
import com.hackathon.video.config.AuthenticationFilter;
import com.hackathon.video.config.SecurityConfig;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoController.class)
@Import({SecurityConfig.class, AuthenticationFilter.class})
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UploadVideoUseCase uploadVideoUseCase;
    @MockBean private GetVideoUseCase getVideoUseCase;
    @MockBean private ProcessingResultUseCase updateVideoStatusUseCase;
    @MockBean private DownloadVideoUseCase downloadVideoUseCase;
    @MockBean private DeleteVideoUseCase deleteVideoUseCase;
    @MockBean private RetryVideoUseCase retryVideoUseCase;
    @MockBean private UpdateVideoUseCase updateVideoUseCase;

    private final String VALID_USER_ID = "user123";
    private final String VALID_EMAIL = "user@test.com";

    @Test
    void shouldReturnForbiddenWhenHeadersMissing() throws Exception {
        mockMvc.perform(get("/videos"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUploadVideo() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "new content".getBytes());
        MockMultipartFile title = new MockMultipartFile("title", "", MediaType.TEXT_PLAIN_VALUE, "New Title".getBytes());

        Video video = Video.builder()
                .id(UUID.randomUUID())
                .userId(VALID_USER_ID)
                .title("My Video")
                .status(VideoStatus.PROCESSING)
                .build();

        when(uploadVideoUseCase.execute(eq(VALID_USER_ID), anyString(), any())).thenReturn(video);

        mockMvc.perform(multipart("/videos")
                        .file(file)
                        .file(title)
                        .header("x-user-id", VALID_USER_ID)
                        .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldRetryVideoProcessing() throws Exception {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).status(VideoStatus.PENDING).build();

        when(retryVideoUseCase.execute(id)).thenReturn(video);

        mockMvc.perform(post("/videos/" + id + "/retry")
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetVideoById() throws Exception {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).title("Found").build();
        when(getVideoUseCase.findById(id)).thenReturn(video);

        mockMvc.perform(get("/videos/" + id)
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Found"));
    }

    @Test
    void shouldDownloadVideo() throws Exception {
        UUID id = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("file-content".getBytes());

        FileDownloadResultDTO result = FileDownloadResultDTO.builder()
                .inputStream(is)
                .mimeType("video/mp4")
                .fileName("video.mp4")
                .extension(".mp4")
                .build();

        when(downloadVideoUseCase.downloadVideo(id)).thenReturn(result);

        mockMvc.perform(get("/videos/" + id + "/download")
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDownloadZip() throws Exception {
        UUID id = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("file-content".getBytes());

        FileDownloadResultDTO result = FileDownloadResultDTO.builder()
                .inputStream(is)
                .mimeType("application/zip")
                .fileName("video.zip")
                .extension(".zip")
                .build();

        when(downloadVideoUseCase.downloadZip(id)).thenReturn(result);

        mockMvc.perform(get("/videos/" + id + "/zip")
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldListVideos() throws Exception {
        when(getVideoUseCase.findByUserId("user1")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/videos")
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteVideo() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteVideoUseCase).execute(id);

        mockMvc.perform(delete("/videos/" + id)
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldHandleVideoNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(getVideoUseCase.findById(id)).thenThrow(new com.hackathon.video.exception.VideoNotFoundException("Not found"));
        mockMvc.perform(get("/videos/" + id)
                    .header("x-user-id", VALID_USER_ID)
                    .header("x-user-email", VALID_EMAIL))
                .andExpect(status().isNotFound());
    }
}