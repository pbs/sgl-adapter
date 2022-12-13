package org.pbs.sgladapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Job {
    @JsonProperty("RID")
    public int rID;
    @JsonProperty("RunState")
    public int runState;
    @JsonProperty("ExitState")
    public int exitState;
    @JsonProperty("ExitStateMessage")
    public String exitStateMessage;
    @JsonProperty("QueuedStateCode")
    public int queuedStateCode;
    @JsonProperty("QueuedStateMessage")
    public String queuedStateMessage;
    @JsonProperty("Action")
    public int action;
    @JsonProperty("Status")
    public int status;
    @JsonProperty("DisplayName")
    public String displayName;
    @JsonProperty("Priority")
    public int priority;
    @JsonProperty("ExecutingServer")
    public String executingServer;
    @JsonProperty("Group")
    public String group;
    @JsonProperty("Volume")
    public String volume;
    @JsonProperty("Changer")
    public String changer;
    @JsonProperty("CallingProduct")
    public String callingProduct;
    @JsonProperty("CallingServer")
    public String callingServer;
    @JsonProperty("LFK")
    public int lFK;
    @JsonProperty("QK")
    public int qK;
    @JsonProperty("Size")
    public int size;
    @JsonProperty("BytesTransferred")
    public int bytesTransferred;
    @JsonProperty("BytesVerified")
    public int bytesVerified;
    @JsonProperty("WillVerify")
    public boolean willVerify;
    @JsonProperty("Percent")
    public int percent;
}
