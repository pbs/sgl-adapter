package org.pbs.sgladapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.FileRestoreResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatusResponse;
import org.pbs.sgladapter.service.SglAdapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/sgl")
@Tag(description = "Provides access to Tasks",
        name = "Task Resource")
public class SglAdapterController {

  private static final Logger logger = LoggerFactory.getLogger(SglAdapterController.class);
  private SglAdapterService sglAdapterService;

  private Mapper mapper;

  public SglAdapterController(SglAdapterService sglAdapterService,
                              Mapper mapper) {
    this.sglAdapterService = sglAdapterService;
    this.mapper = mapper;
  }

  @PostMapping("/restore")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400",
                  description = "400 - Bad Request",
                  content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<FileRestoreResponseDto> createRestoreTask(@Valid @RequestBody FileRestoreDto fileRestoreDto)
          throws JsonProcessingException {
    logger.info("Request received {}", fileRestoreDto);
    SglGenericRequest request = mapper.toGenericRequest(fileRestoreDto);

    request = sglAdapterService.createRestoreTask(request);

    // Set TaskId, Status, Details

    FileRestoreResponseDto response = mapper.toFileRestoreResponse(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }


  @PostMapping("/task")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          description = "400 - Bad Request",
          content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<Task> createTask(@Valid @RequestBody Task task)
      throws JsonProcessingException {
    logger.info("Task received {}", task);
    Task retTask = sglAdapterService.createTask(task);
    return new ResponseEntity<>(retTask, HttpStatus.OK);
  }

  @GetMapping("/task/{taskType}/{taskId}")
  public ResponseEntity<TaskStatusResponse> getJobStatusForTaskId(@PathVariable String taskType,
                                                                  @PathVariable String taskId) {
    logger.info("Getting job status for taskId:" + taskType + " - " + taskId);
    TaskStatusResponse taskStatusResponse = sglAdapterService.getJobStatus(taskType, taskId);
    logger.info("Got taskStatusResponse:" + taskStatusResponse);
    return new ResponseEntity<>(taskStatusResponse, HttpStatus.OK);
  }

}
