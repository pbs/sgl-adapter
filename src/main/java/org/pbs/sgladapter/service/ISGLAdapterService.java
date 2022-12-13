package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.model.Task;

public interface ISGLAdapterService {

    public Task createTask(Task task) throws JsonProcessingException;

    public Task getJobStatus(String tasktype, String taskId);
}
