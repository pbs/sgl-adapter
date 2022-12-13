package org.pbs.sgladapter.model.sgl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class SGLPayload {
    private static final Logger logger = LoggerFactory.getLogger(SGLPayload.class);

    @NotBlank(message = "caller must not be empty")
    private String caller;

    private String displayName;

    @Min(value = 1, message = "priority must be greater than 0")
    @Max(value = 5, message = "priority must be less than 6")
    private Integer priority;

    @NotNull(message = "files must not be null")
    private List<SGLFilesPayload> files;

}
