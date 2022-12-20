package org.pbs.sgladapter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        type = "object",
        title = "FileRestoreTaskDetails"
)
public class SGLGenericTaskDetailsRequest {

    @NotBlank(message = "path must not be empty")
    private String path;

    // filename can be empty if it needs to restore 3 files for MOV
    // we shouldn't make it required
    private String filename;

    @NotBlank(message = "resourceId must not be empty")
    private String resourceId;

    private String locatorInfo;

    private Boolean deleteSource;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Hidden
    private String details;
}