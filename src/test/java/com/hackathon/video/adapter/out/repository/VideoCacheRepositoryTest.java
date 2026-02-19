package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.domain.entity.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoCacheRepositoryTest {

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;
    @InjectMocks private VideoCacheRepository repository;

    @Test
    void shouldSaveToCache() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Video video = Video.builder().id(UUID.randomUUID()).build();

        repository.save(video);

        verify(valueOperations).set(anyString(), eq(video), any());
    }

    @Test
    void shouldFindByIdInCache() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).build();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(video);

        java.util.Optional<Video> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldDeleteFromCache() {
        UUID id = UUID.randomUUID();
        repository.delete(id);
        verify(redisTemplate).delete(anyString());
    }
}
