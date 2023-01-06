package org.pbs.sgladapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Value
@Builder
@Jacksonized
public class FileRestoreDetailsRequestDto {

  @NotBlank(message = "path must not be empty")
  private String path;

  @NotBlank(message = "filename must not be empty")
  private String filename;

  @NotEmpty(message = "resourceId must not be empty")
  private String resourceId;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Hidden
  private String details;
}
