package org.pbs.sgladapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.pbs.sgladapter.dto.CreateResponseDto;
import org.pbs.sgladapter.dto.FileArchiveDto;
import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.StatusResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.pbs.sgladapter.service.SglAdapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  @Operation(summary = "Submit a file restore request",
      description = "Provides capability to submit a file restore request to SGL")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "${api.response-codes.created.desc}"),
      @ApiResponse(responseCode = "400",
          description = "${api.response-codes.bad-request.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})}),
      @ApiResponse(responseCode = "503",
          description = "${api.response-codes.internal-server-error.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<CreateResponseDto> createRestoreTask(
      @Valid @RequestBody FileRestoreDto fileRestoreDto,
      @RequestHeader(value = "X-Correlation-ID", required = true) String correlationId)
      throws JsonProcessingException {
    logger.info("X-Correlation-ID : {}", correlationId);
    logger.info("Request received {}", fileRestoreDto);
    SglGenericRequest request = mapper.toGenericRequest(fileRestoreDto);

    request = sglAdapterService.createRestoreRequest(request);

    // Set Response
    CreateResponseDto response = mapper.toFileRestoreResponse(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }


  @PostMapping("/archive")
  @Operation(summary = "Submit a file archive request",
      description = "Provides capability to submit a file archive request to SGL")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "${api.response-codes.created.desc}"),
      @ApiResponse(responseCode = "400",
          description = "${api.response-codes.bad-request.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})}),
      @ApiResponse(responseCode = "503",
          description = "${api.response-codes.internal-server-error.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<CreateResponseDto> createArchiveTask(
      @Valid @RequestBody FileArchiveDto fileArchiveDto,
      @RequestHeader(value = "X-Correlation-ID", required = true) String correlationId)
      throws JsonProcessingException {
    logger.info("X-Correlation-ID : {}", correlationId);
    logger.info("Request received {}", fileArchiveDto);
    SglGenericRequest request = mapper.toGenericRequest(fileArchiveDto);

    request = sglAdapterService.createArchiveRequest(request);

    // Set Response
    CreateResponseDto response = mapper.toFileArchiveResponse(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/status/{jobId}")
  @Operation(summary = "Checks file restore/archive job status for a given jobId",
      description = "Provides capability to check SGL job status to restore/archive")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "${api.response-codes.ok.desc}"),
      @ApiResponse(responseCode = "404",
          description = "${api.response-codes.not-found.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})}),
      @ApiResponse(responseCode = "503",
          description = "${api.response-codes.internal-server-error.desc}",
          content = {@Content(examples = {@ExampleObject(value = "")})})})
  public ResponseEntity<StatusResponseDto> getJobStatusForTaskId(
          @PathVariable String jobId,
          @RequestHeader(value = "X-Correlation-ID", required = true) String correlationId) {
    logger.info("X-Correlation-ID : {}", correlationId);
    logger.info("Getting job status for taskId:" + " - " + jobId);
    SglGenericRequest taskStatusResponse = sglAdapterService.getJobStatus(jobId);
    logger.info("Got taskStatusResponse:" + taskStatusResponse);
    StatusResponseDto response = mapper.toStatusResponseDto(taskStatusResponse, correlationId);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
