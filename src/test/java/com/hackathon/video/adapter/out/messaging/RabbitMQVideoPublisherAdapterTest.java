package com.hackathon.video.adapter.out.messaging;

import com.hackathon.video.domain.entity.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQVideoPublisherAdapterTest {

    @Mock private RabbitTemplate rabbitTemplate;
    @InjectMocks private RabbitMQVideoPublisherAdapter adapter;

    @Test
    void shouldPublishMessage() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).title("Test").build();

        adapter.publishVideoProcessRequest(video);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), captor.capture());

        Object payload = captor.getValue();
        assertNotNull(payload);
        assertInstanceOf(Map.class, payload);

        Map<?, ?> map = (Map<?, ?>) payload;
        assertEquals(id.toString(), map.get("videoId"));
    }
}
