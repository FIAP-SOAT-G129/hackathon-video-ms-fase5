package com.hackathon.video.adapter.out.storage;

import com.hackathon.video.domain.repository.VideoStoragePort;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class LocalStorageAdapter implements VideoStoragePort {

    // Em um cenário real, isso salvaria no S3 ou sistema de arquivos
    @Override
    public String store(InputStream inputStream, String fileName) {
        String path = "/storage/videos/" + UUID.randomUUID() + "_" + fileName;
        System.out.println("Storing file at: " + path);
        return path;
    }

    @Override
    public InputStream retrieve(String storagePath) {
        System.out.println("Retrieving file from: " + storagePath);
        return null; // Mock
    }

    @Override
    public void delete(String storagePath) {
        System.out.println("Deleting file at: " + storagePath);
    }
}
