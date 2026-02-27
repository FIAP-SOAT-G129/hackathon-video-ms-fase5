package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import com.hackathon.video.domain.repository.UserIdentityPort;
import com.hackathon.video.exception.MessagingException;
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

    @Value("${app.services.auth.url}")
    private String authServiceUrl;

    @Override
    public Optional<String> getEmailByUserId(String userId) throws  MessagingException {
        // 1. Try to get from cache
        Optional<String> cachedEmail = cacheRepository.findByUserId(userId);
        if (cachedEmail.isPresent()) {
            log.info("Email found in cache for user: {}", userId);
            return cachedEmail;
        }

        // 2. Fallback: Call the Users API
        log.info("Email not found in cache. Fetching from User API for user: {}", userId);
        Optional<String> emailFromApi = fetchFromExternalApi(userId);
        
        emailFromApi.ifPresent(email -> cacheRepository.save(userId, email));

        return emailFromApi;
    }

    private Optional<String> fetchFromExternalApi(String userId) throws MessagingException {
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
            throw new MessagingException("Failed to fetch user email from Auth Service for userId: " + userId);
        }
        return Optional.empty();
    }
}
