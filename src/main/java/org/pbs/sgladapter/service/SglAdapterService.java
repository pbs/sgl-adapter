package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatusResponse;

public interface SglAdapterService {

  public SglGenericRequest createRestoreRequest(SglGenericRequest genericRequest) throws JsonProcessingException;

  public SglGenericRequest createArchiveRequest(SglGenericRequest genericRequest) throws JsonProcessingException;

  public TaskStatusResponse getJobStatus(String tasktype, String taskId);
}
