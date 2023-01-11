package org.pbs.sgladapter.dto.sgl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class SglPayload {
  private static final Logger logger = LoggerFactory.getLogger(SglPayload.class);

  @NotBlank(message = "caller must not be empty")
  private String caller;

  private String displayName;

  @Min(value = 1, message = "priority must be greater than 0")
  @Max(value = 5, message = "priority must be less than 6")
  private Integer priority;

  @NotNull(message = "files must not be null")
  private List<SglFilesPayload> files;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Hidden
  private String target;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Hidden
  private Boolean deleteFiles;
}
