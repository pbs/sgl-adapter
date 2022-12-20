package org.pbs.sgladapter.model.sgl;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        title = "Files"
)
public class SGLFilesPayload {

    @NotBlank(message = "guid must not be empty")
    private String guid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Hidden
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Hidden
    private String filename;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Hidden
    private String fullFileName;
}
