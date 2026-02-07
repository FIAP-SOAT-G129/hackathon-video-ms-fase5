package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import com.hackathon.video.domain.repository.UserIdentityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdentityAdapter implements UserIdentityPort {

    private final UserEmailCacheRepository cacheRepository;

    @Override
    public Optional<String> getEmailByUserId(String userId) {
        Optional<String> cachedEmail = cacheRepository.findByUserId(userId);
        if (cachedEmail.isPresent()) {
            log.info("Email found in cache for user: {}", userId);
            return cachedEmail;
        }

        log.info("Email not found in cache. Fetching from User API for user: {}", userId);
        String emailFromApi = fetchFromExternalApi(userId);
        
        if (emailFromApi != null) {
            cacheRepository.save(userId, emailFromApi);
            return Optional.of(emailFromApi);
        }

        return Optional.empty();
    }

    private String fetchFromExternalApi(String userId) {
        return userId + "@example.com";
    }
}
