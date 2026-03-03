package com.hackathon.video.adapter.in.dto;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileDownloadResultDTOTest {

    private FileDownloadResultDTO createFullDTO() {
        return FileDownloadResultDTO.builder()
                .inputStream(new ByteArrayInputStream("data".getBytes()))
                .fileName("file.zip")
                .mimeType("application/zip")
                .extension(".zip")
                .build();
    }

    @Test
    void shouldCoverAllBranchesOfEquals() {
        FileDownloadResultDTO dto1 = createFullDTO();
        FileDownloadResultDTO dto2 = createFullDTO();

        assertEquals(dto1, dto1);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, "string");

        dto2.setInputStream(dto1.getInputStream());
        dto2.setFileName(dto1.getFileName());
        dto2.setMimeType(dto1.getMimeType());
        dto2.setExtension(dto1.getExtension());

        assertEquals(dto1, dto2);

        dto2.setFileName("different.zip");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldCoverSettersHashCodeAndToString() {
        FileDownloadResultDTO dto = new FileDownloadResultDTO(
                null, null, null, null
        );

        dto.setInputStream(new ByteArrayInputStream("data".getBytes()));
        dto.setFileName("file");
        dto.setMimeType("mime");
        dto.setExtension(".ext");

        assertDoesNotThrow(dto::hashCode);
        assertNotNull(dto.toString());
    }
}