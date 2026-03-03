package com.hackathon.video.adapter.out.storage;

import com.fiap.soat.storage.VideoStorageService;
import com.fiap.soat.storage.local.LocalVideoStorageService;
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

    @Test
    void shouldThrowExceptionWhenDeleteFails() {
        LocalStorageAdapter adapter = createAdapter();
        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.delete(StorageType.VIDEO, "/non/existent/path")
        );
    }

    @Test
    void shouldWrapFileStorageException() {

        VideoStorageService videoStorage = org.mockito.Mockito.mock(VideoStorageService.class);
        VideoStorageService zipStorage = org.mockito.Mockito.mock(VideoStorageService.class);

        LocalStorageAdapter adapter = new LocalStorageAdapter(videoStorage, zipStorage);

        UUID videoId = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("data".getBytes());

        org.mockito.Mockito.when(videoStorage.store(
                org.mockito.Mockito.any(),
                org.mockito.Mockito.any()
        )).thenThrow(
                new com.fiap.soat.storage.exception.FileStorageException(
                        "error",
                        new RuntimeException()
                )
        );

        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.store(videoId, is, ".mp4")
        );
    }

    @Test
    void shouldRetrieveFromZipStorage() throws Exception {

        VideoStorageService videoStorage = org.mockito.Mockito.mock(VideoStorageService.class);
        VideoStorageService zipStorage = org.mockito.Mockito.mock(VideoStorageService.class);

        LocalStorageAdapter adapter = new LocalStorageAdapter(videoStorage, zipStorage);

        InputStream expected = new ByteArrayInputStream("zip".getBytes());

        org.mockito.Mockito.when(zipStorage.retrieve("file.zip"))
                .thenReturn(expected);

        InputStream result = adapter.retrieve(StorageType.ZIP, "file.zip");

        assertNotNull(result);
        assertEquals("zip", new String(result.readAllBytes()));
    }
}
