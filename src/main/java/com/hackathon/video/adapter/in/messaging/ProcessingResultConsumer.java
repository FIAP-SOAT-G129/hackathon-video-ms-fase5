package com.hackathon.video.adapter.in.messaging;

import com.hackathon.video.adapter.in.dto.ProcessingRequestDTO;
import com.hackathon.video.application.usecase.ProcessingResultUseCase;
import com.hackathon.video.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingResultConsumer {

    private final ProcessingResultUseCase processingResultUseCase;

    @RabbitListener(queues = RabbitMQConfig.RESULT_QUEUE_KEY)
    public void consume(ProcessingRequestDTO request) {
        log.info("Received processing result for video: {}", request.getVideoId());
        try {
            processingResultUseCase.execute(UUID.fromString(request.getVideoId()), request);
            log.info("Processing result handled successfully for video: {}", request.getVideoId());
        } catch (Exception e) {
            log.error("Error handling processing result for video: {}", request.getVideoId(), e);
            // Lança exceção para permitir retry do RabbitMQ se necessário
            throw e;
        }
    }
}
