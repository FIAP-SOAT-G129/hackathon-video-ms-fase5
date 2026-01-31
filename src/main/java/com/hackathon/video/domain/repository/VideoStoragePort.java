package com.hackathon.video.domain.repository;

import java.io.InputStream;

public interface VideoStoragePort {
    String store(InputStream inputStream, String fileName);
    InputStream retrieve(String storagePath);
    void delete(String storagePath);
}
