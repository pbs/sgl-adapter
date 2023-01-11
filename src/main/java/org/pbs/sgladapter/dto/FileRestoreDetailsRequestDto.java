package org.pbs.sgladapter.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class FileRestoreDetailsRequestDto {

  @NotBlank(message = "requestor must not be empty")
  private String requestor;

  @NotBlank(message = "path must not be empty")
  private String path;

  @NotBlank(message = "filename must not be empty")
  private String filename;

  @NotBlank(message = "resourceId must not be empty")
  private String resourceId;

}
