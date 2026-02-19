package com.hackathon.video.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackathon.video.domain.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingRequestDTO {
    private String videoId;
    private VideoStatus status;
    private String zipPath;
    private String errorMessage;
}
