package org.pbs.sgladapter.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pbs.sgladapter.model.sgl.SGLStatusResponse;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SGLGenericTaskResponse extends Task {
    private SGLStatusResponse taskDetails;

}