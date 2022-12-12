package org.pbs.sgladapter.service;

import org.pbs.sgladapter.model.Task;

public interface ISGLAdapterService {

    public Task createTask(Task task);

    public Task getJobStatus(String tasktype, String taskId);
}
