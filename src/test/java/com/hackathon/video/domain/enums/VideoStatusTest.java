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

    @Test
    void shouldReturnTrueWhenMimeTypeSupported() {
        assertTrue(SupportedVideoFormat.isSupported("video/mp4"));
        assertTrue(SupportedVideoFormat.isSupported("VIDEO/MP4"));
    }

    @Test
    void shouldReturnFalseWhenMimeTypeNotSupported() {
        assertFalse(SupportedVideoFormat.isSupported("video/avi"));
    }

    @Test
    void shouldReturnTrueWhenExtensionSupported() {
        assertTrue(SupportedVideoFormat.isSupportedExtension(".mp4"));
        assertTrue(SupportedVideoFormat.isSupportedExtension(".MP4"));
    }

    @Test
    void shouldReturnFalseWhenExtensionNotSupported() {
        assertFalse(SupportedVideoFormat.isSupportedExtension(".avi"));
    }

    @Test
    void shouldReturnEnumWhenMimeTypeValid() {
        SupportedVideoFormat format =
                SupportedVideoFormat.fromMimeType("video/mp4");

        assertEquals(SupportedVideoFormat.MP4, format);
    }

    @Test
    void shouldThrowExceptionWhenMimeTypeInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SupportedVideoFormat.fromMimeType("video/avi")
        );

        assertTrue(exception.getMessage().contains("Unsupported mime type"));
    }

    @Test
    void shouldReturnEnumWhenExtensionValid() {
        SupportedVideoFormat format =
                SupportedVideoFormat.fromExtension(".mp4");

        assertEquals(SupportedVideoFormat.MP4, format);
    }

    @Test
    void shouldThrowExceptionWhenExtensionInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SupportedVideoFormat.fromExtension(".avi")
        );

        assertTrue(exception.getMessage().contains("Unsupported file extension"));
    }
}