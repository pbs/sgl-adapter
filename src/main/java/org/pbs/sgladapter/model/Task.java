package org.pbs.sgladapter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.Hidden;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.data.mongodb.core.mapping.Document;

//@Document("tasks")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SglGenericTaskRequest.class, name = "FileRestore"),
    @JsonSubTypes.Type(value = SglGenericTaskRequest.class, name = "FileArchive")
})
public abstract class Task {
  private static final Logger logger = LoggerFactory.getLogger(Task.class);

  @Pattern(
      regexp = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$",
      message = "correlationId must be a valid GUID")
  private String correlationId;

  // Will be null or empty until Task has been submitted to backend vendor system
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Hidden
  private String taskId;

  @Pattern.List({@Pattern(regexp = "FileRestore|FileArchive")})
  //@Pattern.List({@Pattern(regexp = "FileRestore")})
  private String type;

  @Min(value = 1, message = "priority must be greater than 0")
  @Max(value = 100, message = "priority must be less than 101")
  private int priority;

  private TaskStatus status;
}
