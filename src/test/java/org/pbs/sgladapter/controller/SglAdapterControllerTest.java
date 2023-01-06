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
import org.pbs.sgladapter.dto.CreateResponseDto;
import org.pbs.sgladapter.dto.StatusResponseDto;
import org.pbs.sgladapter.model.*;
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
    /*
    SglGenericRequest task = new SglGenericRequest();
    task.setTaskId("123");
    task.setType(FILE_RESTORE.getType());
    when(sglAdapterService.createRestoreRequest(any())).thenReturn(task);
    ResponseEntity<CreateResponseDto> responseEntity = sglAdapterController.createRestoreTask(any());
    assertEquals("123", ((CreateResponseDto) responseEntity.getBody()).getTaskId());

     */
  }

  @Test
  void testCreateTaskArchive() throws Exception {
    /*
    SglGenericRequest task = new SglGenericRequest();
    task.setTaskId("123");
    task.setType(FILE_ARCHIVE.getType());
    when(sglAdapterService.createArchiveRequest(any())).thenReturn(task);
    ResponseEntity<CreateResponseDto> responseEntity = sglAdapterController.createArchiveTask(any());
    assertEquals("123", ((CreateResponseDto) responseEntity.getBody()).getTaskId());

     */
  }

  @Test
  void testGetJobStatusForTaskId() throws Exception {
    SglGenericRequest sampleTaskStatusResponse =
            new SglGenericRequest();
    sampleTaskStatusResponse.setStatus(TaskStatus.COMPLETED_SUCCESS);
    when(sglAdapterService.getJobStatus(any())).thenReturn(sampleTaskStatusResponse);
    ResponseEntity<StatusResponseDto> responseEntity =
            sglAdapterController.getJobStatusForTaskId(any());
    assertEquals(TaskStatus.COMPLETED_SUCCESS,
            ((StatusResponseDto) responseEntity.getBody()).getStatus());
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }
}
