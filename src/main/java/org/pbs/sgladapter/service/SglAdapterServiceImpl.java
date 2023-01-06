package org.pbs.sgladapter.service;

import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.pbs.sgladapter.adapter.SglAdapterClient;
import org.pbs.sgladapter.exception.ValidationFailedException;
import org.pbs.sgladapter.model.*;
import org.pbs.sgladapter.model.sgl.Job;
import org.pbs.sgladapter.model.sgl.SglFilesPayload;
import org.pbs.sgladapter.model.sgl.SglPayload;
import org.pbs.sgladapter.model.sgl.SglStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SglAdapterServiceImpl implements SglAdapterService {

  private static final Logger logger
      = LoggerFactory.getLogger(SglAdapterServiceImpl.class);

  private SglAdapterClient sglAdapterClient;

  public SglAdapterServiceImpl(SglAdapterClient sglAdapterClient) {
    this.sglAdapterClient = sglAdapterClient;
  }

  @Override
  public Task createTask(Task task) throws JsonProcessingException, ValidationFailedException {
    logger.info("inside of Service.createTask");
    String response = "";
    String request = prepareCreateTaskRequest(task);
    logger.info("Type is : {}", task.getType());
    logger.info("Request is : {}", request);
    if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
      response = sglAdapterClient.restore(request);
    } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {
      response = sglAdapterClient.archive(request);
    }
    logger.info(response);

    task = prepareCreateTaskResponse(response, task);

    return task;
  }

  @Override
  public SglGenericRequest createRestoreTask(SglGenericRequest genericRequest) throws JsonProcessingException {

    SglFilesPayload sglFilesPayload = SglFilesPayload.builder()
            .guid(genericRequest.getResourceId())
            .build();
    sglFilesPayload.setPath(genericRequest.getPath());
    sglFilesPayload.setFilename(genericRequest.getFilename());

    SglPayload sglPayload = SglPayload.builder()
            .caller(genericRequest.getCorrelationId())
            .displayName(genericRequest.getResourceId())
            .priority(genericRequest.getPriority())
            .files(List.of(sglFilesPayload))
            .build();
    ObjectMapper om = new ObjectMapper();
    String request = null;
    try {
      request = om.writeValueAsString(sglPayload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    String response = sglAdapterClient.restore(request);

    genericRequest = prepareCreateTaskResponseNew(response, genericRequest);

    return genericRequest;
    //return request;
  }

  private String prepareCreateTaskRequest(Task task) throws ValidationFailedException {

    // Validate Data and throw exception if it's missing a value
    String requiredData = validateData(task);

    if (!StringUtils.isBlank(requiredData)) {
      throw new ValidationFailedException(requiredData);
    }

    String resourceId = ((SglGenericTaskRequest) task).getTaskDetails().getResourceId();

    SglFilesPayload sglFilesPayload = SglFilesPayload.builder()
        .guid(resourceId)
        .build();
    SglPayload sglPayload = null;

    if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
      sglFilesPayload.setPath(((SglGenericTaskRequest) task).getTaskDetails().getPath());
      sglFilesPayload.setFilename(((SglGenericTaskRequest) task).getTaskDetails().getFilename());

      sglPayload = SglPayload.builder()
          .caller(task.getCorrelationId())
          .displayName(resourceId)
          .priority(task.getPriority())
          .files(List.of(sglFilesPayload))
          .build();
    } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {
      String path = ((SglGenericTaskRequest) task).getTaskDetails().getPath();
      if (path != null && !path.isBlank()) {
        if (!path.trim().endsWith("\\")) {
          path = path.trim().concat("\\");
        }
      }
      String fileName = ((SglGenericTaskRequest) task).getTaskDetails().getFilename();
      String fullFileName = (path == null) ? "" : path + fileName;
      sglFilesPayload.setFullFileName(fullFileName);

      sglPayload = SglPayload.builder()
          .caller(task.getCorrelationId())
          .displayName(resourceId)
          .priority(task.getPriority())
          .target(((SglGenericTaskRequest) task).getTaskDetails().getLocatorInfo())
          .deleteFiles(((SglGenericTaskRequest) task).getTaskDetails().getDeleteSource())
          .files(List.of(sglFilesPayload))
          .build();
    }

    ObjectMapper om = new ObjectMapper();
    String request = null;
    try {
      request = om.writeValueAsString(sglPayload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return request;

  }


  private SglGenericRequest prepareCreateTaskResponseNew(String response, SglGenericRequest genericRequest) {
    if (!StringUtils.isBlank(response)) {
      ObjectMapper jsonMapper = new JsonMapper();
      JsonNode json = null;
      TaskStatus status = TaskStatus.COMPLETED_SUCCESS;
      try {
        json = jsonMapper.readTree(response);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      String taskId = json.get("RID").asText();
      if (Integer.parseInt(taskId) <= 0) {
        status = TaskStatus.COMPLETED_FAILED;
        logger.error("Failed to create a new task for " + genericRequest.getType()
                + " [" + response + "]");
      }

      genericRequest.setTaskId(taskId);
      genericRequest.setStatus(status);
      genericRequest.setDetails(response);
    }

    return genericRequest;
  }

  private Task prepareCreateTaskResponse(String response, Task task) {
    if (!StringUtils.isBlank(response)) {
      ObjectMapper jsonMapper = new JsonMapper();
      JsonNode json = null;
      TaskStatus status = TaskStatus.COMPLETED_SUCCESS;
      try {
        json = jsonMapper.readTree(response);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      String taskId = json.get("RID").asText();
      if (Integer.parseInt(taskId) <= 0) {
        status = TaskStatus.COMPLETED_FAILED;
        logger.error("Failed to create a new task for " + task.getType()
            + " [" + response + "]");
      }
      task.setTaskId(taskId);
      task.setStatus(status);
      ((SglGenericTaskRequest) task).getTaskDetails().setDetails(response);

    }

    return task;
  }

  private String validateData(Task task) {

    String requiredData = "";

    // Validate CorrelationId
    String correlationId = task.getCorrelationId();
    if (StringUtils.isBlank(correlationId)) {
      requiredData = requiredData.concat(", CorrelationId");
    }

    // Validate ResourceId
    String resourceId = ((SglGenericTaskRequest) task).getTaskDetails().getResourceId();

    if (StringUtils.isBlank(resourceId)) {
      requiredData = requiredData.concat(", ResourceId");
    }

    // Validate Path
    String path = ((SglGenericTaskRequest) task).getTaskDetails().getPath();
    if (StringUtils.isBlank(path)) {
      requiredData = requiredData.concat(", Path");
    }

    // Validate Filename
    String filename = ((SglGenericTaskRequest) task).getTaskDetails().getFilename();
    if (StringUtils.isBlank(filename)) {
      requiredData = requiredData.concat(", Filename");
    } else {
      if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {
        // Validate Filename - must have file extension
        if (!filename.matches("[^\\s]+(\\.(?i)[^\\s]+$)")) {
          requiredData = requiredData.concat(", Filename");
        }
      }
    }

    if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {

      // Validate LocatorInfo
      String locatorInfo = ((SglGenericTaskRequest) task).getTaskDetails().getLocatorInfo();

      if (StringUtils.isBlank(locatorInfo)) {
        requiredData = requiredData.concat(", LocatorInfo");
      }
    }

    if (!StringUtils.isBlank(requiredData)) {
      requiredData = requiredData.replaceFirst(", ", "");
    }

    return requiredData;
  }

  @Override
  public TaskStatusResponse getJobStatus(String taskType, String taskId) {
    logger.info("inside of Service.getJobStatus");

    String response = sglAdapterClient.getTaskStatus(taskId);

    logger.info(response);

    TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

    try {
      ObjectMapper om = new ObjectMapper();

      SglStatusResponse sglStatusResponse = om.readValue(response, SglStatusResponse.class);
      Job job = sglStatusResponse.getJob();

      int exitState = 1;
      if (job != null) {
        exitState = job.getExitState();
      }

      //The finished status of the job. One of the following values:
      // 0 (NO_SUCH_JOB)
      // 1 (LIVE)
      // 2 (KILLED)
      // 3 (STOPPED)
      // 4 (FAILED)
      // 5 (PASSED)
      // +6 (PASSED_WITH_WARNING
      if (exitState == 1) {
        taskStatus = TaskStatus.IN_PROGRESS;
      } else if (exitState >= 5) {
        taskStatus = TaskStatus.COMPLETED_SUCCESS;
      } else {
        taskStatus = TaskStatus.COMPLETED_FAILED;
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    TaskStatusResponse taskStatusResponse = new TaskStatusResponse(taskStatus);

    return taskStatusResponse;
  }
}
