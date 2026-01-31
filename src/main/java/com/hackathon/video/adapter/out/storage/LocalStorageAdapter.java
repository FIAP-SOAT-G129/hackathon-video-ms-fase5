package com.hackathon.video.adapter.out.storage;

import com.hackathon.video.domain.repository.VideoStoragePort;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.UUID;

@Component
public class LocalStorageAdapter implements VideoStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Override
    public String store(InputStream inputStream, String fileName) {
        String path = "/storage/videos/" + UUID.randomUUID() + "_" + fileName;
        log.info("Storing file at: {}", path);
        return path;
    }

    @Override
    public InputStream retrieve(String storagePath) {
        log.info("Retrieving file from: {}", storagePath);
        return null; // Mock
    }

    @Override
    public void delete(String storagePath) {
        log.info("Deleting file at: {}", storagePath);
    }
}
