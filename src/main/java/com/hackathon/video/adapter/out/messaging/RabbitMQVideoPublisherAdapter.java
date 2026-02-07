package com.hackathon.video.adapter.out.messaging;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoMessagePublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RabbitMQVideoPublisherAdapter implements VideoMessagePublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQVideoPublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "video.processing.exchange";
    private static final String ROUTING_KEY = "video.processing.request";

    @Override
    public void publishVideoProcessRequest(Video video) {
        Map<String, Object> message = new HashMap<>();
        message.put("videoId", video.getId().toString());
        message.put("userId", video.getUserId());
        message.put("inputVideoPath", video.getStoragePath());
        
        Map<String, Object> params = new HashMap<>();
        params.put("frameInterval", Optional.of(10));
        params.put("outputFormat", "zip");
        message.put("extractionParams", params);

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
        log.info("Published processing request for video: {}", video.getId());
    }
}
