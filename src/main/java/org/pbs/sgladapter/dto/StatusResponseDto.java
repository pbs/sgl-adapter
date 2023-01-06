package org.pbs.sgladapter.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.pbs.sgladapter.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class StatusResponseDto {

    String requestorCorrelationId;
    TaskStatus status;
    LocalDateTime timestamp;
    String resourceId;
    Map<String, String> error;
    String responseDetails;
}
