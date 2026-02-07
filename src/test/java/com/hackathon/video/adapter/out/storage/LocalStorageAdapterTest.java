package com.hackathon.video.adapter.out.storage;

import com.hackathon.video.domain.enums.StorageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageAdapterTest {

    private final LocalStorageAdapter adapter = new LocalStorageAdapter();

    @TempDir
    Path tempDir;

    @Test
    void shouldStoreAndRetrieveVideoFile() throws Exception {
        ReflectionTestUtils.setField(adapter, "storageVideosDir", tempDir.toString());

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
}
