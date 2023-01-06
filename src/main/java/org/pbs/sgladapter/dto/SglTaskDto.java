package org.pbs.sgladapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pbs.sgladapter.model.TaskStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SglTaskDto {



    @Pattern(
            regexp = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$",
            message = "correlationId must be a valid GUID")
    private String correlationId;

    @Pattern.List({@Pattern(regexp = "file_restore")})
    private String type;

    @Min(value = 1, message = "priority must be greater than 0")
    @Max(value = 100, message = "priority must be less than 101")
    private int priority;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Hidden
    private String taskId;

    private TaskStatus status;
}
