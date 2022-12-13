package org.pbs.sgladapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SGLStatusResponse {
    @JsonProperty("Job")
    public Job job;
    @JsonProperty("Success")
    public boolean success;
    @JsonProperty("Errors")
    public ArrayList<Object> errors;
    @JsonProperty("Message")
    public String message;
    @JsonProperty("Lid")
    public String lid;
}
