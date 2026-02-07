package com.hackathon.video.adapter.in.dto;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

@Data
@Builder
public class FileDownloadResult {
    private InputStream inputStream;
    private String fileName;
    private String mimeType;
    private String extension;
}
