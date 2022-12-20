package org.pbs.sgladapter.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.model.*;
import org.pbs.sgladapter.service.ISGLAdapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

@ExtendWith(MockitoExtension.class)
class SGLAdapterControllerTest {

    @InjectMocks
    SGLAdapterController sglAdapterController;

    @Mock
    ISGLAdapterService sglAdapterService;

    @Test
    void testCreateTaskRestore() throws Exception {
        Task task = new SGLGenericTaskRequest();
        task.setTaskId("123");
        task.setType(FILE_RESTORE.getType());
        when(sglAdapterService.createTask(any())).thenReturn(task);
        ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
        assertEquals("123", ((SGLGenericTaskRequest) responseEntity.getBody()).getTaskId());
        assertEquals(FILE_RESTORE.getType(), ((SGLGenericTaskRequest) responseEntity.getBody()).getType());
    }

    @Test
    void testCreateTaskArchive() throws Exception {
        Task task = new SGLGenericTaskRequest();
        task.setTaskId("123");
        task.setType(FILE_ARCHIVE.getType());
        when(sglAdapterService.createTask(any())).thenReturn(task);
        ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
        assertEquals("123", ((SGLGenericTaskRequest) responseEntity.getBody()).getTaskId());
        assertEquals(FILE_ARCHIVE.getType(), ((SGLGenericTaskRequest) responseEntity.getBody()).getType());
    }

    @Test
    void testGetJobStatusForTaskId() throws Exception {
        TaskStatusResponse sampleTaskStatusResponse = new TaskStatusResponse(TaskStatus.COMPLETED_SUCCESS);
        when(sglAdapterService.getJobStatus(any(), any())).thenReturn(sampleTaskStatusResponse);
        ResponseEntity<TaskStatusResponse> responseEntity = sglAdapterController.getJobStatusForTaskId(any(), any());
        assertEquals(TaskStatus.COMPLETED_SUCCESS, ((TaskStatusResponse) responseEntity.getBody()).getStatus());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}