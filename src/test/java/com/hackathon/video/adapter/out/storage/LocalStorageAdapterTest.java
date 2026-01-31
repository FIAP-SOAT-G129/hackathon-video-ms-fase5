package com.hackathon.video.adapter.out.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageAdapterTest {

    private final LocalStorageAdapter adapter = new LocalStorageAdapter();

    @TempDir
    Path tempDir;

    @Test
    void shouldStoreAndRetrieveFile() throws Exception {
        ReflectionTestUtils.setField(adapter, "storageBaseDir", tempDir.toString());
        String fileName = "test.txt";
        InputStream is = new ByteArrayInputStream("hello".getBytes());

        String path = adapter.store(is, fileName);
        assertNotNull(path);
        assertTrue(path.contains(fileName));

        InputStream retrieved = adapter.retrieve(path);
        assertNotNull(retrieved);
        assertEquals("hello", new String(retrieved.readAllBytes()));
    }
}
