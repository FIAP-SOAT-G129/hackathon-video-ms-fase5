package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityAdapterTest {

    @Mock
    private UserEmailCacheRepository cacheRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UserIdentityAdapter identityAdapter;

    private final String AUTH_URL = "http://auth-service";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(identityAdapter, "authServiceUrl", AUTH_URL);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnEmailFromCache() {
        when(cacheRepository.findByUserId("user123")).thenReturn(Optional.of("cached@example.com"));

        Optional<String> result = identityAdapter.getEmailByUserId("user123");

        assertTrue(result.isPresent());
        assertEquals("cached@example.com", result.get());
        verify(cacheRepository, never()).save(anyString(), anyString());
        verifyNoInteractions(restTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmailFromApiAndSaveToCache() {
        String userId = "user123";
        String email = "user123@example.com";
        String url = AUTH_URL + "/auth/me";
        String token = "mock-token";

        when(cacheRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn(token);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(response);
        
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        Optional<String> result = identityAdapter.getEmailByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(email, result.get());
        verify(cacheRepository).save(userId, email);
    }

    @Test
    void shouldReturnEmptyWhenNoJwtInContext() {
        String userId = "user123";
        when(cacheRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> result = identityAdapter.getEmailByUserId(userId);

        assertTrue(result.isEmpty());
        verifyNoInteractions(restTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyWhenApiReturnsNull() {
        String userId = "user123";
        String url = AUTH_URL + "/auth/me";
        String token = "mock-token";

        when(cacheRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn(token);
        
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(null));

        Optional<String> result = identityAdapter.getEmailByUserId(userId);

        assertTrue(result.isEmpty());
    }
}
