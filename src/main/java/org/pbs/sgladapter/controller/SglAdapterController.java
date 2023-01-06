package org.pbs.sgladapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

import org.pbs.sgladapter.dto.FileArchiveDto;
import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.CreateResponseDto;
import org.pbs.sgladapter.dto.StatusResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
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
  public ResponseEntity<CreateResponseDto> createRestoreTask(@Valid @RequestBody FileRestoreDto fileRestoreDto)
          throws JsonProcessingException {
    logger.info("Request received {}", fileRestoreDto);
    SglGenericRequest request = mapper.toGenericRequest(fileRestoreDto);

    request = sglAdapterService.createRestoreRequest(request);

    // Set Response
    CreateResponseDto response = mapper.toFileRestoreResponse(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }


  @PostMapping("/archive")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400",
                  description = "400 - Bad Request",
                  content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<CreateResponseDto> createArchiveTask(@Valid @RequestBody FileArchiveDto fileArchiveDto)
          throws JsonProcessingException {
    logger.info("Request received {}", fileArchiveDto);
    SglGenericRequest request = mapper.toGenericRequest(fileArchiveDto);

    request = sglAdapterService.createArchiveRequest(request);

    // Set Response
    CreateResponseDto response = mapper.toFileArchiveResponse(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/status/{taskId}")
  public ResponseEntity<StatusResponseDto> getJobStatusForTaskId(@PathVariable String taskId) {
    logger.info("Getting job status for taskId:" + " - " + taskId);
    SglGenericRequest taskStatusResponse = sglAdapterService.getJobStatus(taskId);
    logger.info("Got taskStatusResponse:" + taskStatusResponse);
    StatusResponseDto response = mapper.toStatusResponseDto(taskStatusResponse);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
