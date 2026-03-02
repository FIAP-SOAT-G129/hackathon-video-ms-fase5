package com.hackathon.video.adapter.out.storage;

import com.fiap.soat.storage.VideoStorageService;
import com.fiap.soat.storage.exception.FileDeletionException;
import com.fiap.soat.storage.exception.FileRetrievalException;
import com.fiap.soat.storage.exception.FileStorageException;
import com.fiap.soat.storage.exception.StoredFileNotFoundException;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.enums.SupportedVideoFormat;
import com.hackathon.video.domain.repository.VideoStoragePort;
import com.hackathon.video.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class LocalStorageAdapter implements VideoStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    private final VideoStorageService videoStorage;
    private final VideoStorageService zipStorage;

    public LocalStorageAdapter(
            @Qualifier("videoStorage") VideoStorageService videoStorage,
            @Qualifier("zipStorage") VideoStorageService zipStorage) {
        this.videoStorage = videoStorage;
        this.zipStorage = zipStorage;
    }

    private VideoStorageService getStorage(StorageType type) {
        return switch (type) {
            case VIDEO -> videoStorage;
            case ZIP -> zipStorage;
        };
    }

    @Override
    public String store(UUID videoId, InputStream inputStream, String extension) {
        if (inputStream == null) {
            throw new StorageException("Input stream must not be null");
        }

        if (extension == null || extension.isEmpty() || !SupportedVideoFormat.isSupportedExtension(extension)) {
            throw new StorageException("File extension must not be null or empty");
        }

        try {
            String storedPath = videoStorage.store(inputStream, videoId + extension);
            log.info("File stored successfully: {}", storedPath);
            return storedPath;
        } catch (FileStorageException e) {
            log.error("Failed to store file", e);
            throw new StorageException("Failed to store file");
        }
    }

    @Override
    public InputStream retrieve(StorageType type, String storagePath) {
        try {
            return getStorage(type).retrieve(storagePath);
        } catch (StoredFileNotFoundException | FileRetrievalException e) {
            log.error("Failed to read file: {}", storagePath);
            throw new StorageException("Failed to read file");
        }
    }

    @Override
    public void delete(StorageType type, String storagePath) {
        try {
            getStorage(type).delete(storagePath);
            log.info("File deleted: {}", storagePath);
        } catch (FileDeletionException e) {
            log.error("Failed to delete file: {}", storagePath);
            throw new StorageException("Failed to delete file");
        }
    }
}
