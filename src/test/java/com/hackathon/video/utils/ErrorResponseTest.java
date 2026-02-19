package com.hackathon.video.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse() {
        String message = "Test error message";
        ErrorResponse response = new ErrorResponse(message);

        assertEquals(message, response.message());
    }
}
