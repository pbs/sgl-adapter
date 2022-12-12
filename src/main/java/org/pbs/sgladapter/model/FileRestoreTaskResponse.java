package org.pbs.sgladapter.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FileRestoreTaskResponse extends Task {
    private FileRestoreTaskDetailsResponse taskDetails;

}