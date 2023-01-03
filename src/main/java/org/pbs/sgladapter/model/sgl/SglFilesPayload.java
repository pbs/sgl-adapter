package org.pbs.sgladapter.model.sgl;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
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
public class SglFilesPayload {

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
