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
}
