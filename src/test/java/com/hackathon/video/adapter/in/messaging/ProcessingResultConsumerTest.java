package com.hackathon.video.adapter.in.messaging;

import com.hackathon.video.adapter.in.dto.ProcessingRequestDTO;
import com.hackathon.video.application.usecase.ProcessingResultUseCase;
import com.hackathon.video.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessingResultConsumerTest {

    @Mock
    private ProcessingResultUseCase processingResultUseCase;

    @InjectMocks
    private ProcessingResultConsumer consumer;

    @Test
    void shouldConsumeAndExecuteUseCase() {
        UUID videoId = UUID.randomUUID();
        ProcessingRequestDTO request = ProcessingRequestDTO.builder()
                .videoId(videoId.toString())
                .status(VideoStatus.DONE)
                .zipPath("/path/to/zip.zip")
                .build();

        consumer.consume(request);

        verify(processingResultUseCase).execute(videoId, request);
    }

    @Test
    void shouldThrowExceptionWhenUseCaseFails() {
        UUID videoId = UUID.randomUUID();
        ProcessingRequestDTO request = ProcessingRequestDTO.builder().videoId(videoId.toString()).build();
        doThrow(new RuntimeException("Error")).when(processingResultUseCase).execute(eq(videoId), any());
        assertThrows(RuntimeException.class, () -> consumer.consume(request));
    }
}
