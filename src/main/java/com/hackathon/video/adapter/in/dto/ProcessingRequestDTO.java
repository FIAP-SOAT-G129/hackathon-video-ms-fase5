package com.hackathon.video.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackathon.video.domain.enums.VideoStatus;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ProcessingRequestDTO {
    private VideoStatus status;
    private String zipPath;
    private String errorMessage;
}
