package com.hackathon.video.domain.entity;

import com.hackathon.video.domain.enums.VideoStatus;
import com.hackathon.video.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

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
        Video video = Video.builder()
                .title("Title")
                .fileName("file.mp4")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowExceptionWhenTitleIsMissing() {
        Video video = Video.builder()
                .userId("u")
                .fileName("f")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowExceptionWhenFileNameIsMissing() {
        Video video = Video.builder()
                .userId("u")
                .title("t")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowWhenUserIdIsBlank() {
        Video video = Video.builder()
                .userId("   ")
                .title("Title")
                .fileName("file.mp4")
                .mimeType("video/mp4")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowWhenTitleIsBlank() {
        Video video = Video.builder()
                .userId("user")
                .title("   ")
                .fileName("file.mp4")
                .mimeType("video/mp4")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowWhenFileNameIsBlank() {
        Video video = Video.builder()
                .userId("user")
                .title("Title")
                .fileName("   ")
                .mimeType("video/mp4")
                .build();

        assertThrows(BusinessException.class, video::validate);
    }

    @Test
    void shouldThrowWhenSettingInvalidMimeType() {
        Video video = new Video();
        assertThrows(IllegalArgumentException.class,
                () -> video.setMimeType("video/avi"));
    }

    @Test
    void shouldThrowWhenBuilderReceivesInvalidMimeType() {
        assertThrows(IllegalArgumentException.class, () ->
                Video.builder()
                        .userId("user")
                        .title("Title")
                        .fileName("file.mp4")
                        .mimeType("video/avi")
                        .build()
        );
    }

    @Test
    void shouldThrowWhenSettingStoragePathWithInvalidMimeType() {
        Video invalid = new Video();
        assertThrows(IllegalArgumentException.class,
                () -> invalid.setStoragePath("/path"));
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
        assertNotNull(video.getUpdatedAt());
    }

    @Test
    void shouldGetExtension() {
        Video video = Video.builder()
                .mimeType("video/mp4")
                .build();

        assertEquals(".mp4", video.getExtension());
    }

    @Test
    void shouldCoverBuilderToString() {
        Video.VideoBuilder builder = Video.builder()
                .userId("user")
                .title("Title")
                .fileName("file.mp4")
                .mimeType("video/mp4");

        assertNotNull(builder.toString());
    }

    @Test
    void shouldTestAllSettersAndGetters() {
        Video video = new Video();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        video.setId(id);
        video.setUserId("user");
        video.setTitle("title");
        video.setFileName("file");
        video.setMimeType("video/mp4");
        video.setStoragePath("/path");
        video.setZipPath("/zip");
        video.setStatus(VideoStatus.DONE);
        video.setErrorMessage("err");
        video.setCreatedAt(now);
        video.setUpdatedAt(now);

        assertEquals(id, video.getId());
        assertEquals("user", video.getUserId());
        assertEquals("title", video.getTitle());
        assertEquals("file", video.getFileName());
        assertEquals("video/mp4", video.getMimeType());
        assertEquals("/path", video.getStoragePath());
        assertEquals("/zip", video.getZipPath());
        assertEquals(VideoStatus.DONE, video.getStatus());
        assertEquals("err", video.getErrorMessage());
        assertEquals(now, video.getCreatedAt());
        assertEquals(now, video.getUpdatedAt());
        assertNotNull(video.toString());
    }

    @Test
    void shouldReturnTrueWhenComparingSameInstance() {
        Video video = new Video();
        assertEquals(video, video);
    }

    @Test
    void shouldReturnFalseWhenComparingWithNull() {
        Video video = new Video();
        assertNotEquals(video, null);
    }

    @Test
    void shouldReturnFalseWhenComparingDifferentClass() {
        Video video = new Video();
        assertNotEquals(video, "string");
    }

    @Test
    void shouldReturnFalseWhenFieldsAreDifferent() {
        Video video1 = Video.builder()
                .userId("u1")
                .title("t1")
                .fileName("f1")
                .mimeType("video/mp4")
                .build();

        Video video2 = Video.builder()
                .userId("u2")
                .title("t2")
                .fileName("f2")
                .mimeType("video/mp4")
                .build();

        assertNotEquals(video1, video2);
    }

    @Test
    void shouldReturnFalseWhenOneFieldIsNullAndOtherNot() {
        Video v1 = new Video();
        v1.setId(UUID.randomUUID());

        Video v2 = new Video();

        assertNotEquals(v1, v2);
    }

    @Test
    void shouldReturnFalseWhenStatusDifferent() {
        Video v1 = new Video();
        v1.setStatus(VideoStatus.PENDING);

        Video v2 = new Video();
        v2.setStatus(VideoStatus.ERROR);

        assertNotEquals(v1, v2);
    }

    @Test
    void shouldCoverHashCodeWithNullFields() {
        Video video = new Video();
        assertDoesNotThrow(video::hashCode);
    }

    @Test
    void shouldReturnTrueWhenAllFieldsAreEqual() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Video v1 = new Video(id, "user", "title", "file",
                "video/mp4", "/path", "/zip",
                VideoStatus.DONE, "err", now, now);

        Video v2 = new Video(id, "user", "title", "file",
                "video/mp4", "/path", "/zip",
                VideoStatus.DONE, "err", now, now);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}