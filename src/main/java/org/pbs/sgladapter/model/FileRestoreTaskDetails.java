package org.pbs.sgladapter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        type = "object",
        title = "FileRestoreTaskDetails"
)
public class FileRestoreTaskDetails {
    private String filename;
    private String locatorInfo;
    private boolean deleteSource;
}