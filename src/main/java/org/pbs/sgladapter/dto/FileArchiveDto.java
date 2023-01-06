package org.pbs.sgladapter.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Value
@Builder
@Jacksonized
public class FileArchiveDto {

    @Pattern(
            regexp = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$",
            message = "correlationId must be a valid GUID")
    private String correlationId;

    @Pattern.List({@Pattern(regexp = "file_archive")})
    private String type;

    @Min(value = 1, message = "priority must be greater than 0")
    @Max(value = 100, message = "priority must be less than 101")
    private int priority;

    @Valid
    private FileArchiveDetailsRequestDto taskDetails;

}
