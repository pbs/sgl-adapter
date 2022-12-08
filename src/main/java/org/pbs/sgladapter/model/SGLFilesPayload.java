package org.pbs.sgladapter.model;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        title = "Files"
)
public class SGLFilesPayload {

    private String guid;
    private String path;
    private String filename;
}
