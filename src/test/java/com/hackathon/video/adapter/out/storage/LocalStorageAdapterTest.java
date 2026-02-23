package com.hackathon.video.adapter.out.storage;

import br.com.fiap.storage.local.LocalVideoStorageService;
import com.hackathon.video.domain.enums.StorageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageAdapterTest {

    private LocalStorageAdapter adapter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        adapter = new LocalStorageAdapter(new LocalVideoStorageService(tempDir.toString()));
        ReflectionTestUtils.setField(adapter, "storageVideosDir", tempDir.toString());
        ReflectionTestUtils.setField(adapter, "storageZipsDir", tempDir.toString());
    }

    @Test
    void shouldStoreAndRetrieveVideoFile() throws Exception {

        String extension = ".mp4";
        UUID videoId = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("hello".getBytes());

        String fileName = adapter.store(videoId, is, extension);
        assertNotNull(fileName);
        assertTrue(fileName.contains(videoId.toString()));
        assertTrue(fileName.contains(extension));

        InputStream retrieved = adapter.retrieve(StorageType.VIDEO, fileName);
        assertNotNull(retrieved);
        assertEquals("hello", new String(retrieved.readAllBytes()));
    }

    @Test
    void shouldThrowExceptionWhenStoreFails() {
        UUID videoId = UUID.randomUUID();
        InputStream is = new ByteArrayInputStream("hello".getBytes());

        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.store(videoId, is, "/../../etc/passwd")
        );
    }

    @Test
    void shouldThrowExceptionWhenInputStreamIsNull() {
        assertThrows(com.hackathon.video.exception.StorageException.class, () ->
                adapter.store(UUID.randomUUID(), null, ".mp4")
        );
    }

    @Test
    void testResolveInternalPathSecurityViolation() {
        assertThrows(com.hackathon.video.exception.StorageException.class, () -> adapter.retrieve(StorageType.VIDEO, "../secret.txt"));
    }

    @Test
    void shouldDeleteFile() throws Exception {
        String fileName = "test.mp4";
        java.nio.file.Files.write(tempDir.resolve(fileName), "content".getBytes());

        assertDoesNotThrow(() -> adapter.delete(StorageType.VIDEO, fileName));
    }
}
