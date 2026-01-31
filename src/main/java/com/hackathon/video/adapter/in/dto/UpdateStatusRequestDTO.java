package com.hackathon.video.adapter.in.dto;

import com.hackathon.video.domain.enums.VideoStatus;
import lombok.Data;

@Data
public class UpdateStatusRequestDTO {
    private VideoStatus status;
    private String errorMessage;
}
