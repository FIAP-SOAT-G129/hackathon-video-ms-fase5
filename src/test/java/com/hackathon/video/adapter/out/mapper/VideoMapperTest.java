package com.hackathon.video.adapter.out.mapper;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VideoMapperTest {

    private final VideoMapper mapper = new VideoMapper();

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        VideoEntity entity = new VideoEntity();
        entity.setId(id);
        entity.setUserId("user123");
        entity.setTitle("Test Video");
        entity.setOriginalFileName("test.mp4");
        entity.setStoragePath("/path/test.mp4");
        entity.setStatus(VideoStatus.PENDING);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        Video domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("user123", domain.getUserId());
        assertEquals("Test Video", domain.getTitle());
        assertEquals(VideoStatus.PENDING, domain.getStatus());
    }

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Video domain = Video.builder()
                .id(id)
                .userId("user123")
                .title("Test Video")
                .originalFileName("test.mp4")
                .storagePath("/path/test.mp4")
                .status(VideoStatus.COMPLETED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        VideoEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("user123", entity.getUserId());
        assertEquals(VideoStatus.COMPLETED, entity.getStatus());
    }
}
