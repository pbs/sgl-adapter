package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatusResponse;

public interface SglAdapterService {

  public Task createTask(Task task) throws JsonProcessingException;

  public SglGenericRequest createRestoreTask(SglGenericRequest task) throws JsonProcessingException;

  public TaskStatusResponse getJobStatus(String tasktype, String taskId);
}
