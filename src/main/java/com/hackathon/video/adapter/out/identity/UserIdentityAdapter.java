package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import com.hackathon.video.domain.repository.UserIdentityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdentityAdapter implements UserIdentityPort {

    private final UserEmailCacheRepository cacheRepository;
    private final RestTemplate restTemplate;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Override
    public Optional<String> getEmailByUserId(String userId) {
        // 1. Tenta buscar no cache
        Optional<String> cachedEmail = cacheRepository.findByUserId(userId);
        if (cachedEmail.isPresent()) {
            log.info("Email found in cache for user: {}", userId);
            return cachedEmail;
        }

        // 2. Fallback: Chamada para API de Users
        log.info("Email not found in cache. Fetching from User API for user: {}", userId);
        Optional<String> emailFromApi = fetchFromExternalApi(userId);
        
        emailFromApi.ifPresent(email -> cacheRepository.save(userId, email));

        return emailFromApi;
    }

    private Optional<String> fetchFromExternalApi(String userId) {
        try {
            String url = authServiceUrl + "/users/" + userId;
            log.info("Fetching user info from Auth Service: {}", url);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("email")) {
                return Optional.of((String) response.get("email"));
            }
        } catch (Exception e) {
            log.error("Error fetching user email from Auth Service for userId: {}", userId, e);
        }
        return Optional.empty();
    }
}
