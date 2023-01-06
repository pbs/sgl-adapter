package org.pbs.sgladapter.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.pbs.sgladapter.model.TaskStatus;

import java.util.List;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class FileRestoreResponseDto {
    String taskId;
    String correlationId;
    String type;
    int priority;
    TaskStatus status;
    Map<String, String> taskDetails;
}
