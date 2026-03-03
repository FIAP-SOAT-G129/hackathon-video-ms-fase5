package com.hackathon.video.adapter.out.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserEmailCacheRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private UserEmailCacheRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        repository = new UserEmailCacheRepository(redisTemplate);
    }

    @Test
    void shouldSaveEmailWithCorrectKeyAndTTL() {

        String userId = "user123";
        String email = "user@test.com";

        repository.save(userId, email);

        verify(valueOperations).set(
                "user:email:" + userId,
                email,
                Duration.ofDays(1)
        );
    }

    @Test
    void shouldReturnEmailWhenFound() {

        String userId = "user123";
        String email = "user@test.com";

        when(valueOperations.get("user:email:" + userId))
                .thenReturn(email);

        Optional<String> result = repository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(email, result.get());
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {

        String userId = "user123";

        when(valueOperations.get("user:email:" + userId))
                .thenReturn(null);

        Optional<String> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());
    }
}