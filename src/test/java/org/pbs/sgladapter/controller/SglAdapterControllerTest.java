package org.pbs.sgladapter.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.dto.CreateResponseDto;
import org.pbs.sgladapter.dto.FileArchiveDto;
import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.StatusResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.pbs.sgladapter.model.TaskStatus;
import org.pbs.sgladapter.service.SglAdapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

@ExtendWith(MockitoExtension.class)
class SglAdapterControllerTest {

  @InjectMocks
  SglAdapterController sglAdapterController;

  @Mock
  SglAdapterService sglAdapterService;

  @Mock
  Mapper mapper;

  @Test
  void testCreateTaskRestore() throws Exception {
    SglGenericRequest task = new SglGenericRequest();
    task.setTaskId("123");
    task.setType(FILE_RESTORE.getType());

    CreateResponseDto createResponseDto = CreateResponseDto.builder()
            .taskId("123")
            .type(FILE_RESTORE.getType())
            .taskDetails(Map.of("path", "\\file\\restore\\path","resourceId", "abc1234", "filename", "abc1234.mxf"))
            .build();
    when(sglAdapterService.createRestoreRequest(any())).thenReturn(task);
    when(mapper.toFileRestoreResponse(any())).thenReturn(createResponseDto);
    ResponseEntity<CreateResponseDto> responseEntity = sglAdapterController.createRestoreTask(any(FileRestoreDto.class), any(String.class));
    assertEquals("123", ((CreateResponseDto) responseEntity.getBody()).getTaskId());
    assertEquals(FILE_RESTORE.getType(), ((CreateResponseDto) responseEntity.getBody()).getType());
    assertEquals("\\file\\restore\\path", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("path"));
    assertEquals("abc1234", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("resourceId"));
    assertEquals("abc1234.mxf", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("filename"));

  }

  @Test
  void testCreateTaskArchive() throws Exception {
    SglGenericRequest task = new SglGenericRequest();
    task.setTaskId("123");
    task.setType(FILE_ARCHIVE.getType());

    CreateResponseDto createResponseDto = CreateResponseDto.builder()
            .taskId("123")
            .type(FILE_ARCHIVE.getType())
            .taskDetails(Map.of("path", "\\file\\restore\\path","resourceId", "abc1234", "filename", "abc1234.mxf", "locatorInfo", "aaa111"))
            .build();
    when(sglAdapterService.createArchiveRequest(any())).thenReturn(task);
    when(mapper.toFileArchiveResponse(any())).thenReturn(createResponseDto);
    ResponseEntity<CreateResponseDto> responseEntity = sglAdapterController.createArchiveTask(any(FileArchiveDto.class), any(String.class));
    assertEquals("123", ((CreateResponseDto) responseEntity.getBody()).getTaskId());
    assertEquals(FILE_ARCHIVE.getType(), ((CreateResponseDto) responseEntity.getBody()).getType());
    assertEquals("\\file\\restore\\path", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("path"));
    assertEquals("abc1234", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("resourceId"));
    assertEquals("abc1234.mxf", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("filename"));
    assertEquals("aaa111", ((CreateResponseDto) responseEntity.getBody()).getTaskDetails().get("locatorInfo"));
  }

  @Test
  void testGetJobStatusForTaskId() throws Exception {

    StatusResponseDto statusResponseDto = StatusResponseDto.builder()
            .status(TaskStatus.COMPLETED_SUCCESS)
            .build();
    SglGenericRequest sampleTaskStatusResponse =
            new SglGenericRequest();
    sampleTaskStatusResponse.setStatus(TaskStatus.COMPLETED_SUCCESS);
    when(sglAdapterService.getJobStatus(any())).thenReturn(sampleTaskStatusResponse);
    when(mapper.toStatusResponseDto(any(SglGenericRequest.class), any(String.class))).thenReturn(statusResponseDto);
    ResponseEntity<StatusResponseDto> responseEntity =
            sglAdapterController.getJobStatusForTaskId(any(), any());
    assertEquals(TaskStatus.COMPLETED_SUCCESS,
            ((StatusResponseDto) responseEntity.getBody()).getStatus());
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }
}
