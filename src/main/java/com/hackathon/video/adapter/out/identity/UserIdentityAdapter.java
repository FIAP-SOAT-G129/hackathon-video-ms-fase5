package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import com.hackathon.video.domain.repository.UserIdentityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

        // 2. Fallback: Chamada para API de Users com JWT Relay
        log.info("Email not found in cache. Fetching from User API for user: {}", userId);
        Optional<String> emailFromApi = fetchFromExternalApi(userId);
        
        emailFromApi.ifPresent(email -> cacheRepository.save(userId, email));

        return emailFromApi;
    }

    private Optional<String> fetchFromExternalApi(String userId) {
        try {
            String token = getJwtFromContext();
            if (token == null) {
                log.warn("No JWT found in SecurityContext for relay");
                return Optional.empty();
            }

            String url = authServiceUrl + "/auth/me"; // Usando /auth/me para pegar os dados do usuário logado
            log.info("Fetching user info from Auth Service with JWT Relay: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();
            
            if (response != null && response.containsKey("email")) {
                return Optional.of((String) response.get("email"));
            }
        } catch (Exception e) {
            log.error("Error fetching user email from Auth Service with JWT Relay for userId: {}", userId, e);
        }
        return Optional.empty();
    }

    private String getJwtFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        return null;
    }
}
