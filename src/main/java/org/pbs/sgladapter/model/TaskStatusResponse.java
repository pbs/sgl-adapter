package org.pbs.sgladapter.model;

public class TaskStatusResponse {
  private TaskStatus status;

  public TaskStatusResponse(TaskStatus status) {
    this.status = status;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}
