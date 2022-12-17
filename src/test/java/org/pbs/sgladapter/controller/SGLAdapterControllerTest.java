package org.pbs.sgladapter.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.model.SGLGenericTaskResponse;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskType;
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
        Task task = new SGLGenericTaskResponse();
        task.setTaskId("123");
        task.setType(FILE_RESTORE.getType());
        when(sglAdapterService.createTask(any())).thenReturn(task);
        ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
        assertEquals("123", ((SGLGenericTaskResponse)responseEntity.getBody()).getTaskId());
        assertEquals(FILE_RESTORE.getType(), ((SGLGenericTaskResponse)responseEntity.getBody()).getType());
    }

    @Test
    void testCreateTaskArchive() throws Exception {
        Task task = new SGLGenericTaskResponse();
        task.setTaskId("123");
        task.setType(FILE_ARCHIVE.getType());
        when(sglAdapterService.createTask(any())).thenReturn(task);
        ResponseEntity<Task> responseEntity = sglAdapterController.createTask(any());
        assertEquals("123", ((SGLGenericTaskResponse)responseEntity.getBody()).getTaskId());
        assertEquals(FILE_ARCHIVE.getType(), ((SGLGenericTaskResponse)responseEntity.getBody()).getType());
    }

    @Test
    void testGetJobStatusForTaskId() throws Exception {
        Task task = new SGLGenericTaskResponse();
        task.setTaskId("123");
        task.setType(TaskType.FILE_RESTORE.getType());
        when(sglAdapterService.getJobStatus(any(), any())).thenReturn(task);
        ResponseEntity<Task> responseEntity = sglAdapterController.getJobStatusForTaskId(any(), any());
        assertEquals("123", ((SGLGenericTaskResponse)responseEntity.getBody()).getTaskId());
        assertEquals("FileRestore", ((SGLGenericTaskResponse)responseEntity.getBody()).getType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}