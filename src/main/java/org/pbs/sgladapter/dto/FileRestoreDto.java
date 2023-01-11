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
public class FileRestoreDto {

    @Pattern(regexp = "file_restore", message = "type must be file_restore")
    private String type;

    @Min(value = 1, message = "priority must be greater than 0")
    @Max(value = 100, message = "priority must be less than 101")
    private int priority;

    @Valid
    private FileRestoreDetailsRequestDto taskDetails;

}
