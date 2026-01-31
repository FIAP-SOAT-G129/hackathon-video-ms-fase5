package com.hackathon.video.adapter.out.storage;

import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalStorageAdapter implements VideoStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Value("${app.storage.local-path:/tmp/videos}")
    private String storageBaseDir;

    @Override
    public String store(InputStream inputStream, String fileName) {
        try {
            Path uploadPath = Paths.get(storageBaseDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            Path filePath = uploadPath.resolve(uniqueFileName);
            
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File stored successfully at: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            throw new StorageException("Could not store file: " + e.getMessage());
        }
    }

    @Override
    public InputStream retrieve(String storagePath) {
        try {
            Path filePath = Paths.get(storagePath);
            if (!Files.exists(filePath)) {
                throw new StorageException("File not found at path: " + storagePath);
            }
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new StorageException("Could not retrieve file: " + e.getMessage());
        }
    }

    @Override
    public void delete(String storagePath) {
        try {
            Path filePath = Paths.get(storagePath);
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", storagePath);
        } catch (IOException e) {
            throw new StorageException("Could not delete file: " + e.getMessage());
        }
    }
}
