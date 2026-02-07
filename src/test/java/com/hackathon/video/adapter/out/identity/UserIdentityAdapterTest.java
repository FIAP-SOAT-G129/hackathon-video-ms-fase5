package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityAdapterTest {

    @Mock
    private UserEmailCacheRepository cacheRepository;

    @InjectMocks
    private UserIdentityAdapter identityAdapter;

    @Test
    void shouldReturnEmailFromCache() {
        when(cacheRepository.findByUserId("user123")).thenReturn(Optional.of("cached@example.com"));

        Optional<String> result = identityAdapter.getEmailByUserId("user123");

        assertTrue(result.isPresent());
        assertEquals("cached@example.com", result.get());
        verify(cacheRepository, never()).save(anyString(), anyString());
    }

    @Test
    void shouldReturnEmailFromApiAndSaveToCache() {
        when(cacheRepository.findByUserId("user123")).thenReturn(Optional.empty());

        Optional<String> result = identityAdapter.getEmailByUserId("user123");

        assertTrue(result.isPresent());
        assertEquals("user123@example.com", result.get());
        verify(cacheRepository).save("user123", "user123@example.com");
    }
}
