package com.hackathon.video.adapter.in.dto;

import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VideoResponseDTOTest {

    private VideoResponseDTO createFullDTO() {
        VideoResponseDTO dto = new VideoResponseDTO();
        dto.setId(UUID.randomUUID());
        dto.setUserId("user");
        dto.setTitle("title");
        dto.setOriginalFileName("file.mp4");
        dto.setStatus(VideoStatus.PROCESSING);
        dto.setErrorMessage("error");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void shouldCoverAllBranchesOfEquals() {
        VideoResponseDTO dto1 = createFullDTO();
        VideoResponseDTO dto2 = createFullDTO();

        assertEquals(dto1, dto1);

        assertNotEquals(dto1, null);

        assertNotEquals(dto1, "string");

        dto2.setId(dto1.getId());
        dto2.setUserId(dto1.getUserId());
        dto2.setTitle(dto1.getTitle());
        dto2.setOriginalFileName(dto1.getOriginalFileName());
        dto2.setStatus(dto1.getStatus());
        dto2.setErrorMessage(dto1.getErrorMessage());
        dto2.setCreatedAt(dto1.getCreatedAt());

        assertEquals(dto1, dto2);

        dto2.setTitle("different");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldCoverHashCodeAndToString() {
        VideoResponseDTO dto = createFullDTO();

        assertDoesNotThrow(dto::hashCode);
        assertNotNull(dto.toString());
    }
}