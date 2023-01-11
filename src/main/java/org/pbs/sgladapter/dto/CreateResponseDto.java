package org.pbs.sgladapter.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.pbs.sgladapter.model.TaskStatus;

@Value
@Builder
@Jacksonized
public class CreateResponseDto {
  String taskId;
  String type;
  int priority;
  TaskStatus status;
  Map<String, Object> taskDetails;
}
