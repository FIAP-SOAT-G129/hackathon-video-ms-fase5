package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.domain.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VideoCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "video:";

    public void save(Video video) {
        redisTemplate.opsForValue().set(KEY_PREFIX + video.getId(), video, Duration.ofMinutes(10));
    }

    public Optional<Video> findById(UUID id) {
        Video video = (Video) redisTemplate.opsForValue().get(KEY_PREFIX + id);
        return Optional.ofNullable(video);
    }

    public void delete(UUID id) {
        redisTemplate.delete(KEY_PREFIX + id);
    }
}
