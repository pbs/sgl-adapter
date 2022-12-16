package org.pbs.sgladapter.model.sgl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class SGLStatusResponse {
    @JsonProperty("Job")
    private Job job;
    @JsonProperty("Success")
    private boolean success;
    @JsonProperty("Errors")
    private ArrayList<Object> errors;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Lid")
    private String lid;
}
