package com.hackathon.video.adapter.out.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LogNotificationAdapterTest {

    private final LogNotificationAdapter adapter = new LogNotificationAdapter();

    @Test
    void shouldLogNotification() {
        assertDoesNotThrow(() -> adapter.send("user1", "Message"));
    }
}
