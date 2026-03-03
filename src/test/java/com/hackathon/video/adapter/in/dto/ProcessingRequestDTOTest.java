package com.hackathon.video.adapter.in.dto;

import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingRequestDTOTest {

    private ProcessingRequestDTO createFullDTO() {
        return new ProcessingRequestDTO(
                "1",
                VideoStatus.DONE,
                "/zip",
                "error"
        );
    }

    @Test
    void shouldCoverAllBranchesOfEquals() {
        ProcessingRequestDTO dto1 = createFullDTO();
        ProcessingRequestDTO dto2 = createFullDTO();

        assertEquals(dto1, dto1);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, "string");

        assertEquals(dto1, dto2);

        dto2.setVideoId("different");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldCoverSettersHashCodeAndToString() {
        ProcessingRequestDTO dto = new ProcessingRequestDTO();

        dto.setVideoId("1");
        dto.setStatus(VideoStatus.PROCESSING);
        dto.setZipPath("/zip");
        dto.setErrorMessage("err");

        assertDoesNotThrow(dto::hashCode);
        assertNotNull(dto.toString());
    }
}