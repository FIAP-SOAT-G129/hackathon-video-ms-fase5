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
}
