package org.pbs.sgladapter.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class SGLPayload {
    private static final Logger logger = LoggerFactory.getLogger(SGLPayload.class);

    private String caller;
    private String displayName;
    private Integer priority;

    private SGLFilesPayload files;
    /*
 "Caller":"Postman Test (adhoc) - bpmuatpu - (Task's correlationId) ",
 "DisplayName":"P222222-555",
 "Priority":60,
 "Files":[
 {
 "Guid":"P222222-555",
 "Path":"\\\\m-isilonsmb\\gpop_dev\\mxf",
 "Filename":"Hello.mxf"
 }
    */
}
