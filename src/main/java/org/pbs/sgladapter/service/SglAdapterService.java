package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.SglTaskDto;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatusResponse;

public interface SglAdapterService {

  public Task createTask(Task task) throws JsonProcessingException;
  public SglTaskDto createRestoreTask(SglTaskDto task) throws JsonProcessingException;

  public TaskStatusResponse getJobStatus(String tasktype, String taskId);
}
