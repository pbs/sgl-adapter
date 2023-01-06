package org.pbs.sgladapter.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class SglGenericRequest {

  private String correlationId;

  private String taskId;

  private String type;

  private int priority;

  private TaskStatus status;

  private String path;

  private String filename;

  private String resourceId;

  private String locatorInfo;

  private Boolean deleteSource;

  private String details;

}
