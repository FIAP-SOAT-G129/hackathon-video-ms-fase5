package com.hackathon.video.adapter.out.entity;

import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VideoEntityTest {

    private VideoEntity createFullEntity() {
        VideoEntity entity = new VideoEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId("user1");
        entity.setTitle("Title");
        entity.setFileName("file.mp4");
        entity.setMimeType("video/mp4");
        entity.setStoragePath("/videos/file.mp4");
        entity.setZipPath("/videos/file.zip");
        entity.setStatus(VideoStatus.PENDING);
        entity.setErrorMessage("error");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void shouldCoverAllBranchesOfEquals() {

        VideoEntity e1 = createFullEntity();
        VideoEntity e2 = createFullEntity();

        assertEquals(e1, e1);
        assertNotEquals(e1, null);
        assertNotEquals(e1, "string");
        assertNotEquals(e1, e2);

        e2.setId(e1.getId());
        e2.setUserId(e1.getUserId());
        e2.setTitle(e1.getTitle());
        e2.setFileName(e1.getFileName());
        e2.setMimeType(e1.getMimeType());
        e2.setStoragePath(e1.getStoragePath());
        e2.setZipPath(e1.getZipPath());
        e2.setStatus(e1.getStatus());
        e2.setErrorMessage(e1.getErrorMessage());
        e2.setCreatedAt(e1.getCreatedAt());
        e2.setUpdatedAt(e1.getUpdatedAt());

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());

        e2.setTitle("Different");
        assertNotEquals(e1, e2);
        e2.setTitle(e1.getTitle());

        e2.setUserId("other");
        assertNotEquals(e1, e2);
        e2.setUserId(e1.getUserId());

        e2.setFileName("other.mp4");
        assertNotEquals(e1, e2);
        e2.setFileName(e1.getFileName());

        e2.setMimeType("video/other");
        assertNotEquals(e1, e2);
        e2.setMimeType(e1.getMimeType());

        e2.setStoragePath("other");
        assertNotEquals(e1, e2);
        e2.setStoragePath(e1.getStoragePath());

        e2.setZipPath("other");
        assertNotEquals(e1, e2);
        e2.setZipPath(e1.getZipPath());

        e2.setStatus(VideoStatus.DONE);
        assertNotEquals(e1, e2);
        e2.setStatus(e1.getStatus());

        e2.setErrorMessage("other");
        assertNotEquals(e1, e2);
        e2.setErrorMessage(e1.getErrorMessage());

        e2.setCreatedAt(LocalDateTime.now().plusDays(1));
        assertNotEquals(e1, e2);
        e2.setCreatedAt(e1.getCreatedAt());

        e2.setUpdatedAt(LocalDateTime.now().plusDays(1));
        assertNotEquals(e1, e2);
    }

    @Test
    void shouldCoverNullFieldsBranches() {

        VideoEntity e1 = new VideoEntity();
        VideoEntity e2 = new VideoEntity();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());

        e1.setTitle("title");
        assertNotEquals(e1, e2);
    }

    @Test
    void shouldCoverToStringAndHashCodeWithNulls() {
        VideoEntity entity = new VideoEntity();
        assertDoesNotThrow(entity::hashCode);
        assertNotNull(entity.toString());
    }

    @Test
    void shouldCoverCanEqual() {
        VideoEntity entity = createFullEntity();
        assertTrue(entity.canEqual(createFullEntity()));
        assertFalse(entity.canEqual("string"));
    }
}