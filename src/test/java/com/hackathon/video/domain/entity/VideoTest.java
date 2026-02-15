package com.hackathon.video.domain.entity;

import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoTest {

    @Test
    void shouldValidateVideo() {
        Video video = Video.builder()
                .userId("user1")
                .title("Title")
                .mimeType("video/mp4")
                .fileName("file.mp4")
                .build();

        assertDoesNotThrow(video::validate);
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsMissing() {
        Video video = Video.builder().title("Title").fileName("file.mp4").build();
        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldUpdateStatus() {
        Video video = new Video();
        video.setStatus(VideoStatus.PROCESSING);
        assertEquals(VideoStatus.PROCESSING, video.getStatus());
        assertNotNull(video.getUpdatedAt());
    }

    @Test
    void shouldMarkAsError() {
        Video video = new Video();
        video.markAsError("error message");
        assertEquals(VideoStatus.ERROR, video.getStatus());
        assertEquals("error message", video.getErrorMessage());
    }

    @Test
    void shouldGetExtension() {
        Video video = Video.builder().mimeType("video/mp4").build();
        assertEquals(".mp4", video.getExtension());
    }

    @Test
    void shouldThrowExceptionWhenTitleIsMissing() {
        Video video = Video.builder().userId("u").fileName("f").build();
        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowExceptionWhenFileNameIsMissing() {
        Video video = Video.builder().userId("u").title("t").build();
        assertThrows(BusinessException.class, video::validate);
    }
}
