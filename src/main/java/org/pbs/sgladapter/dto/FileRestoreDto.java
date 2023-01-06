package org.pbs.sgladapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.pbs.sgladapter.model.SglGenericTaskDetailsRequest;
import org.pbs.sgladapter.model.TaskStatus;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.groups.ConvertGroup;

@Value
@Builder
@Jacksonized
public class FileRestoreDto extends SglTaskDto{
/*
    @Pattern(
            regexp = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$",
            message = "correlationId must be a valid GUID")
    private String correlationId;

    @Pattern.List({@Pattern(regexp = "file_restore")})
    private String type;

    @Min(value = 1, message = "priority must be greater than 0")
    @Max(value = 100, message = "priority must be less than 101")
    private int priority;
*/


    @Valid
    private FileRestoreDetailsRequestDto taskDetails;

}
