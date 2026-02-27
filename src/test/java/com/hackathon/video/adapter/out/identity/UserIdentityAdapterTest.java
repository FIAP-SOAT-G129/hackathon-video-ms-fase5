package com.hackathon.video.adapter.out.identity;

import com.hackathon.video.adapter.out.repository.UserEmailCacheRepository;
import com.hackathon.video.exception.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityAdapterTest {

    @Mock
    private UserEmailCacheRepository cacheRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserIdentityAdapter identityAdapter;

    private final String AUTH_URL = "http://auth-service";
    private final String USER_ID = "user123";
    private final String EMAIL = "user@test.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(identityAdapter, "authServiceUrl", AUTH_URL);
    }

    @Test
    void shouldReturnEmailFromCache() throws MessagingException {
        when(cacheRepository.findByUserId(USER_ID)).thenReturn(Optional.of(EMAIL));

        Optional<String> result = identityAdapter.getEmailByUserId(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(EMAIL, result.get());

        verify(cacheRepository).findByUserId(USER_ID);
        verifyNoInteractions(restTemplate);
        verify(cacheRepository, never()).save(anyString(), anyString());
    }

    @Test
    void shouldFetchFromApiAndSaveToCache() throws MessagingException {
        String expectedUrl = AUTH_URL + "/users/" + USER_ID;
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("email", EMAIL);

        when(cacheRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        Optional<String> result = identityAdapter.getEmailByUserId(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(EMAIL, result.get());
        verify(cacheRepository).save(USER_ID, EMAIL);
    }

    @Test
    void shouldReturnEmptyWhenApiReturnsNoEmailField() throws MessagingException {
        when(cacheRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(new HashMap<>());

        Optional<String> result = identityAdapter.getEmailByUserId(USER_ID);

        assertTrue(result.isEmpty());
        verify(cacheRepository, never()).save(anyString(), anyString());
    }

    @Test
    void shouldReturnEmptyWhenApiReturnsNull() throws MessagingException {
        when(cacheRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        Optional<String> result = identityAdapter.getEmailByUserId(USER_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowMessagingExceptionWhenApiFails() {
        when(cacheRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("Connection Refused"));

        MessagingException exception = assertThrows(MessagingException.class, () -> {
            identityAdapter.getEmailByUserId(USER_ID);
        });

        assertTrue(exception.getMessage().contains("Failed to fetch user email"));
        verify(cacheRepository, never()).save(anyString(), anyString());
    }
}