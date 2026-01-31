package com.hackathon.video.exception;

import com.hackathon.video.utils.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void shouldHandleVideoNotFoundException() {
        VideoNotFoundException ex = new VideoNotFoundException("Not found");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Business error");
        ResponseEntity<ErrorResponse> response = handler.handleBusiness(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
