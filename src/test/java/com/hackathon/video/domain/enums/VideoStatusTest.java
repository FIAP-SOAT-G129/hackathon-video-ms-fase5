package com.hackathon.video.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoStatusTest {

    @Test
    void shouldHaveAllStatus() {
        assertNotNull(VideoStatus.valueOf("PENDING"));
        assertNotNull(VideoStatus.valueOf("PROCESSING"));
        assertNotNull(VideoStatus.valueOf("DONE"));
        assertNotNull(VideoStatus.valueOf("ERROR"));
    }
}
