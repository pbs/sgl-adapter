package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatusResponse;

public interface ISGLAdapterService {

  public Task createTask(Task task) throws JsonProcessingException;

  public TaskStatusResponse getJobStatus(String tasktype, String taskId);
}
