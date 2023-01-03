package org.pbs.sgladapter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.model.SglGenericTaskRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatus;
import org.pbs.sgladapter.model.TaskStatusResponse;
import org.pbs.sgladapter.service.SglAdapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SglAdapterControllerTest {

  @InjectMocks
  SglAdapterController sglAdapterController;

  @Mock
  SglAdapterService sglAdapterService;

  @Test
  void testCreateTaskRestore() throws Exception {
    Task task = new SglGenericTaskRequest();
    task.setTaskId("123");
    task.setType(FILE_RESTORE.getType());
    when(sglAdapterService.createTask(any())).thenReturn(task);
    ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
    assertEquals("123", ((SglGenericTaskRequest) responseEntity.getBody()).getTaskId());
    assertEquals(FILE_RESTORE.getType(),
        ((SglGenericTaskRequest) responseEntity.getBody()).getType());
  }

  @Test
  void testCreateTaskArchive() throws Exception {
    Task task = new SglGenericTaskRequest();
    task.setTaskId("123");
    task.setType(FILE_ARCHIVE.getType());
    when(sglAdapterService.createTask(any())).thenReturn(task);
    ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
    assertEquals("123", ((SglGenericTaskRequest) responseEntity.getBody()).getTaskId());
    assertEquals(FILE_ARCHIVE.getType(),
        ((SglGenericTaskRequest) responseEntity.getBody()).getType());
  }

  @Test
  void testGetJobStatusForTaskId() throws Exception {
    TaskStatusResponse sampleTaskStatusResponse =
        new TaskStatusResponse(TaskStatus.COMPLETED_SUCCESS);
    when(sglAdapterService.getJobStatus(any(), any())).thenReturn(sampleTaskStatusResponse);
    ResponseEntity<TaskStatusResponse> responseEntity =
        sglAdapterController.getJobStatusForTaskId(any(), any());
    assertEquals(TaskStatus.COMPLETED_SUCCESS,
        ((TaskStatusResponse) responseEntity.getBody()).getStatus());
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
