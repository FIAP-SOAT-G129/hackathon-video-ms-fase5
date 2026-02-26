package com.hackathon.video.adapter.out.storage;

import br.com.fiap.storage.VideoStorageService;
import br.com.fiap.storage.local.LocalVideoStorageService;
import com.hackathon.video.domain.enums.StorageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageAdapterTest {

    @TempDir
    Path tempDir;

    private LocalStorageAdapter createAdapter() {
        VideoStorageService videoStorage = new LocalVideoStorageService(tempDir.resolve("videos"));
        VideoStorageService zipStorage = new LocalVideoStorageService(tempDir.resolve("zips"));
        return new LocalStorageAdapter(videoStorage, zipStorage);
    }

    @Test
    void shouldStoreAndRetrieveVideoFile() throws Exception {
        LocalStorageAdapter adapter = createAdapter();

        String extension = ".mp4";
        UUID videoId = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("hello".getBytes());

        String storedPath = adapter.store(videoId, is, extension);
        assertNotNull(storedPath);
        assertTrue(storedPath.contains(videoId.toString()));
        assertTrue(storedPath.contains(extension));

        InputStream retrieved = adapter.retrieve(StorageType.VIDEO, storedPath);
        assertNotNull(retrieved);
        assertEquals("hello", new String(retrieved.readAllBytes()));
    }

    @Test
    void shouldThrowExceptionWhenStoreFails() {
        LocalStorageAdapter adapter = createAdapter();
        UUID videoId = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("hello".getBytes());

        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.store(videoId, is, "/../../etc/passwd")
        );
    }

    @Test
    void shouldThrowExceptionWhenInputStreamIsNull() {
        LocalStorageAdapter adapter = createAdapter();

        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.store(UUID.randomUUID(), null, ".mp4")
        );
    }

    @Test
    void shouldThrowExceptionWhenRetrievingInvalidPath() {
        LocalStorageAdapter adapter = createAdapter();

        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.retrieve(StorageType.VIDEO, tempDir.resolve("nonexistent.mp4").toString())
        );
    }

    @Test
    void shouldDeleteFile() throws Exception {
        LocalStorageAdapter adapter = createAdapter();
        Path videosDir = tempDir.resolve("videos");
        Files.createDirectories(videosDir);
        Path videoPath = videosDir.resolve("test.mp4");
        Files.write(videoPath, "content".getBytes());

        adapter.delete(StorageType.VIDEO, videoPath.toString());

        assertFalse(Files.exists(videoPath));
    }
}
