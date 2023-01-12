package org.pbs.sgladapter.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
@Builder
@Jacksonized
public class FileArchiveDetailsRequestDto {

  @NotBlank(message = "requestor must not be empty")
  private String requestor;

  @NotBlank(message = "path must not be empty")
  private String path;

  @NotBlank(message = "filename must not be empty")
  @Pattern(regexp = "[^\\s]+(\\.(?i)[^\\s]+$)", message = "file name must contain file extension")
  private String filename;

  @NotBlank(message = "resourceId must not be empty")
  private String resourceId;

  @NotBlank(message = "locatorInfo must not be empty")
  private String locatorInfo;

  @NotBlank(message = "deleteSource must not be empty")
  @Pattern(regexp = "^(\\\")?(?i)(\\s)*(true|false)(\\s)*(\\\")?$", message = "deleteSource must be true/false")
  private String deleteSource;

}
