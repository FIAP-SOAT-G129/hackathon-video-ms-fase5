package com.hackathon.video.domain.repository;

import com.hackathon.video.domain.enums.StorageType;

import java.io.InputStream;
import java.util.UUID;

public interface VideoStoragePort {
    String store(UUID videoId, InputStream inputStream, String extension);
    InputStream retrieve(StorageType type, String fileName);
    void delete(StorageType type, String fileName);
}
