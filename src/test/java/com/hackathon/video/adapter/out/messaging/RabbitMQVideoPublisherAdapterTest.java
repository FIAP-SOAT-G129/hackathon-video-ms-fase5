package com.hackathon.video.adapter.out.messaging;

import com.hackathon.video.domain.entity.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQVideoPublisherAdapterTest {

    @Mock private RabbitTemplate rabbitTemplate;
    @InjectMocks private RabbitMQVideoPublisherAdapter adapter;

    @Test
    void shouldPublishMessage() {
        Video video = Video.builder().title("Test").build();
        adapter.publish(video);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), eq(video));
    }
}
