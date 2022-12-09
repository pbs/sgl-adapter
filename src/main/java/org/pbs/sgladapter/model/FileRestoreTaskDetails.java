package org.pbs.sgladapter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        type = "object",
        title = "FileRestoreTaskDetails"
)
public class FileRestoreTaskDetails {

    @NotBlank(message = "path must not be empty")
    private String path;

    @NotBlank(message = "filename must not be empty")
    private String filename;

    @NotBlank(message = "resourceId must not be empty")
    private String resourceId;

    private String locatorInfo;

    private boolean deleteSource;
}