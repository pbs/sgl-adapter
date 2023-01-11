package org.pbs.sgladapter.dto.sgl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Job {
  @JsonProperty("RID")
  private String rid;
  @JsonProperty("RunState")
  private int runState;
  @JsonProperty("ExitState")
  private int exitState;
  @JsonProperty("ExitStateMessage")
  private String exitStateMessage;
  @JsonProperty("QueuedStateCode")
  private int queuedStateCode;
  @JsonProperty("QueuedStateMessage")
  private String queuedStateMessage;
  @JsonProperty("Action")
  private int action;
  @JsonProperty("Status")
  private int status;
  @JsonProperty("DisplayName")
  private String displayName;
  @JsonProperty("Priority")
  private int priority;
  @JsonProperty("ExecutingServer")
  private String executingServer;
  @JsonProperty("Group")
  private String group;
  @JsonProperty("Volume")
  private String volume;
  @JsonProperty("Changer")
  private String changer;
  @JsonProperty("CallingProduct")
  private String callingProduct;
  @JsonProperty("CallingServer")
  private String callingServer;
  @JsonProperty("LFK")
  private int lfk;
  @JsonProperty("QK")
  private int qk;
  @JsonProperty("Size")
  private int size;
  @JsonProperty("BytesTransferred")
  private int bytesTransferred;
  @JsonProperty("BytesVerified")
  private int bytesVerified;
  @JsonProperty("WillVerify")
  private boolean willVerify;
  @JsonProperty("Percent")
  private int percent;
}
