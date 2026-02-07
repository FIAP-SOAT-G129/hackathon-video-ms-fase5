package com.hackathon.video.adapter.out.storage;

import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.SupportedVideoFormat;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Component
public class LocalStorageAdapter implements VideoStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Value("${app.storage.videos-path:/tmp/videos}")
    private String storageVideosDir;

    @Value("${app.storage.zips-path:/tmp/zips}")
    private String storageZipsDir;

    @Override
    public String store(UUID videoId, InputStream inputStream, String extension) {
        if (inputStream == null) {
            throw new StorageException("Input stream must not be null");
        }

        if(extension == null || extension.isEmpty() || !SupportedVideoFormat.isSupportedExtension(extension)) {
            throw new StorageException("File extension must not be null or empty");
        }

        Path destination = resolveInternalPath(StorageType.VIDEO,videoId +  extension);

        try {
            Files.createDirectories(destination);
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored successfully: {}", destination);

            return destination.toString();
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new StorageException("Failed to store file");
        }
    }

    @Override
    public InputStream retrieve(StorageType type, String fileName) {
        Path path = resolveInternalPath(type, fileName);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("Failed to read file: {}", fileName);
            throw new StorageException("Failed to read file");
        }
    }

    @Override
    public void delete(StorageType type, String fileName) {
        Path path = resolveInternalPath(type, fileName);

        try {
            Files.deleteIfExists(path);
            log.info("File deleted: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName);
            throw new StorageException("Failed to delete file");
        }
    }

    private Path resolveInternalPath(StorageType type, String fileName) {
        Path root = switch (type) {
            case VIDEO -> Paths.get(storageVideosDir);
            case ZIP -> Paths.get(storageZipsDir);
        };

        Path normalizedRoot = root.toAbsolutePath().normalize();
        Path resolved = normalizedRoot.resolve(fileName).normalize();

        if (!resolved.startsWith(normalizedRoot)) {
            throw new StorageException("Security violation: invalid file path");
        }

        return resolved;
    }
}
