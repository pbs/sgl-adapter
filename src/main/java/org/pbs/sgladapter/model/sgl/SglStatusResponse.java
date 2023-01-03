package org.pbs.sgladapter.model.sgl;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.Data;

@Data
public class SglStatusResponse {
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
