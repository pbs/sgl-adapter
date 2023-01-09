package org.pbs.sgladapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Value
@Builder
@Jacksonized
public class FileArchiveDetailsRequestDto {

  @NotBlank(message = "path must not be empty")
  private String path;

  @NotBlank(message = "filename must not be empty")
  @Pattern(regexp = "[^\\s]+(\\.(?i)[^\\s]+$)", message = "file name must contain file extension")
  private String filename;

  @NotBlank(message = "resourceId must not be empty")
  private String resourceId;

  @NotBlank(message = "locatorInfo must not be empty")
  private String locatorInfo;

  private Boolean deleteSource;

}
