package com.hackathon.video.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestTemplateConfigTest {

    private final RestTemplateConfig config = new RestTemplateConfig();

    @Test
    void shouldCreateRestTemplate() {
        RestTemplate restTemplate = config.restTemplate();
        assertNotNull(restTemplate);
    }
}
