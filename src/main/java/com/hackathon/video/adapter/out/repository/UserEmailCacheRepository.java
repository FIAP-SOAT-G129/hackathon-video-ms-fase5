package com.hackathon.video.adapter.out.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserEmailCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "user:email:";

    public void save(String userId, String email) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, email, Duration.ofDays(1));
    }

    public Optional<String> findByUserId(String userId) {
        String email = (String) redisTemplate.opsForValue().get(KEY_PREFIX + userId);
        return Optional.ofNullable(email);
    }
}
