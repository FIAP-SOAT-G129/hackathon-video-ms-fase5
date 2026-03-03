package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.adapter.in.dto.FileDownloadResultDTO;
import com.hackathon.video.application.usecase.*;
import com.hackathon.video.config.JwtAuthenticationFilter;
import com.hackathon.video.config.SecurityConfig;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@TestPropertySource(properties = {
        "jwt.secret=12345678901234567890123456789012"
})
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

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_USER_ID = "user123";

    private String generateToken(String userId) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(userId)
                .signWith(key)
                .compact();
    }

    @Test
    void shouldReturnForbiddenWhenTokenMissing() throws Exception {
        mockMvc.perform(get("/videos"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUploadVideo() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "video.mp4", "video/mp4", "new content".getBytes());

        MockMultipartFile title = new MockMultipartFile(
                "title", "", MediaType.TEXT_PLAIN_VALUE, "New Title".getBytes());

        Video video = Video.builder()
                .id(UUID.randomUUID())
                .userId(VALID_USER_ID)
                .title("My Video")
                .status(VideoStatus.PROCESSING)
                .build();

        when(uploadVideoUseCase.execute(eq(VALID_USER_ID), anyString(), any()))
                .thenReturn(video);

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(multipart("/videos")
                        .file(file)
                        .file(title)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldRetryVideoProcessing() throws Exception {
        UUID id = UUID.randomUUID();
        Video video = Video.builder()
                .id(id)
                .status(VideoStatus.PENDING)
                .build();

        when(retryVideoUseCase.execute(id)).thenReturn(video);

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(post("/videos/" + id + "/retry")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldGetVideoById() throws Exception {
        UUID id = UUID.randomUUID();
        Video video = Video.builder()
                .id(id)
                .title("Found")
                .build();

        when(getVideoUseCase.findById(id)).thenReturn(video);

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(get("/videos/" + id)
                        .header("Authorization", "Bearer " + token))
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

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(get("/videos/" + id + "/download")
                        .header("Authorization", "Bearer " + token))
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

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(get("/videos/" + id + "/zip")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldListVideos() throws Exception {
        when(getVideoUseCase.findByUserId(VALID_USER_ID))
                .thenReturn(Collections.emptyList());

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(get("/videos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteVideo() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteVideoUseCase).execute(id);

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(delete("/videos/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateVideo() throws Exception {
        UUID id = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "video.mp4", "video/mp4", "new content".getBytes());
        MockMultipartFile title = new MockMultipartFile(
                "title", "", MediaType.TEXT_PLAIN_VALUE, "Updated Title".getBytes());

        Video video = Video.builder()
                .id(id)
                .title("Updated Title")
                .status(VideoStatus.PROCESSING)
                .build();

        when(updateVideoUseCase.execute(eq(id), anyString(), any())).thenReturn(video);

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(multipart("/videos/" + id)
                        .file(file)
                        .file(title)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldHandleVideoNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(getVideoUseCase.findById(id))
                .thenThrow(new com.hackathon.video.exception.VideoNotFoundException("Not found"));

        String token = generateToken(VALID_USER_ID);

        mockMvc.perform(get("/videos/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowBadCredentialsWhenUserIdIsNullOnUpload() throws Exception {

        VideoController controller = new VideoController(
                uploadVideoUseCase,
                getVideoUseCase,
                downloadVideoUseCase,
                deleteVideoUseCase,
                retryVideoUseCase,
                updateVideoUseCase
        );

        MockMultipartFile file = new MockMultipartFile(
                "file", "video.mp4", "video/mp4", "content".getBytes()
        );

        assertThrows(
                BadCredentialsException.class,
                () -> controller.upload(null, "title", file)
        );
    }

    @Test
    void shouldThrowBadCredentialsWhenUserIdIsNullOnList() {

        VideoController controller = new VideoController(
                uploadVideoUseCase,
                getVideoUseCase,
                downloadVideoUseCase,
                deleteVideoUseCase,
                retryVideoUseCase,
                updateVideoUseCase
        );

        assertThrows(
                BadCredentialsException.class,
                () -> controller.list(null)
        );
    }
}
