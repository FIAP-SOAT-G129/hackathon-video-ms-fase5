package com.hackathon.video.adapter.in.controller;

import com.hackathon.video.application.usecase.*;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UploadVideoUseCase uploadVideoUseCase;
    @MockBean
    private GetVideoUseCase getVideoUseCase;
    @MockBean
    private UpdateVideoStatusUseCase updateVideoStatusUseCase;
    @MockBean
    private DownloadVideoUseCase downloadVideoUseCase;
    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @Test
    void shouldUploadVideo() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", "content".getBytes());
        Video video = Video.builder().id(UUID.randomUUID()).userId("user1").title("Title").build();

        when(uploadVideoUseCase.execute(anyString(), anyString(), anyString(), any(InputStream.class))).thenReturn(video);

        mockMvc.perform(multipart("/videos/user/user1")
                        .file(file)
                        .param("title", "Title"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldListVideos() throws Exception {
        when(getVideoUseCase.findByUserId("user1")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/videos/user/user1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetVideoById() throws Exception {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).build();
        when(getVideoUseCase.findById(id)).thenReturn(video);

        mockMvc.perform(get("/videos/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateStatus() throws Exception {
        UUID id = UUID.randomUUID();
        String body = "{\"status\":\"" + VideoStatus.DONE.name() + "\",\"errorMessage\":\"\"}";

        doNothing().when(updateVideoStatusUseCase).execute(any(UUID.class), any(), anyString());

        mockMvc.perform(patch("/videos/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(updateVideoStatusUseCase, times(1)).execute(id, VideoStatus.DONE, "");
    }

    @Test
    void shouldDownloadOriginalAndZip() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] content = "file-content".getBytes();

        when(downloadVideoUseCase.downloadOriginal(id)).thenReturn(new ByteArrayInputStream(content));
        when(downloadVideoUseCase.downloadZip(id)).thenReturn(new ByteArrayInputStream(content));

        mockMvc.perform(get("/videos/" + id + "/download"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(content));

        mockMvc.perform(get("/videos/" + id + "/zip"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(content));
    }

    @Test
    void shouldDeleteVideo() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteVideoUseCase).execute(id);

        mockMvc.perform(delete("/videos/" + id))
                .andExpect(status().isNoContent());

        verify(deleteVideoUseCase, times(1)).execute(id);
    }
}
