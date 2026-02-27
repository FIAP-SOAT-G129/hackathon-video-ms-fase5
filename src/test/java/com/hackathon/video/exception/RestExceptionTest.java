package com.hackathon.video.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    @DisplayName("404 - VideoNotFoundException")
    void shouldHandleNotFound() {
        var ex = new VideoNotFoundException("Video not found");
        var response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Video not found", response.getBody().message());
    }

    @Test
    @DisplayName("400 - BusinessException")
    void shouldHandleBusiness() {
        var ex = new BusinessException("Business error");
        var response = handler.handleBusiness(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Business error", response.getBody().message());
    }

    @Test
    @DisplayName("503 - StorageException")
    void shouldHandleStorage() {
        var ex = new StorageException("Disk full");
        var response = handler.handleStorage(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Storage Error: Disk full", response.getBody().message());
    }

    @Test
    @DisplayName("503 - MessagingException")
    void shouldHandleMessaging() {
        var ex = new MessagingException("Kafka down");
        var response = handler.handleMessaging(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Messaging Error: Kafka down", response.getBody().message());
    }

    @Test
    @DisplayName("413 - MaxUploadSizeExceededException")
    void shouldHandleMaxSize() {
        var ex = new MaxUploadSizeExceededException(1024L);
        var response = handler.handleMaxSize(ex);

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, response.getStatusCode());
        assertTrue(response.getBody().message().contains("File size exceeds limit"));
    }

    @Test
    @DisplayName("401 - BadCredentialsException")
    void shouldHandleBadCredentials() {
        var ex = new BadCredentialsException("Invalid User");
        var response = handler.handleBadCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized: Invalid User", response.getBody().message());
    }

    @Test
    @DisplayName("500 - General Exception")
    void shouldHandleGeneral() {
        var ex = new RuntimeException("Fatal error");
        var response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().message().contains("An unexpected error occurred"));
        assertTrue(response.getBody().message().contains("Fatal error"));
    }
}