package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.exception.ValidationFailedException;
import org.pbs.sgladapter.model.*;
import org.pbs.sgladapter.model.sgl.Job;
import org.pbs.sgladapter.model.sgl.SGLFilesPayload;
import org.pbs.sgladapter.model.sgl.SGLPayload;
import org.pbs.sgladapter.model.sgl.SGLStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

@Service
public class SGLAdapterService implements ISGLAdapterService {

    private static final Logger logger
            = LoggerFactory.getLogger(SGLAdapterService.class);

    private SGLAdapterClient sglAdapterClient;

    public SGLAdapterService(SGLAdapterClient sglAdapterClient) {
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

    private String prepareCreateTaskRequest(Task task) throws ValidationFailedException {

        // Validate Data and throw exception if it's missing a value
        String requiredData = validateData(task);

        if (!StringUtils.isBlank(requiredData)) {
            throw new ValidationFailedException(requiredData);
        }

        String resourceId = ((SGLGenericTaskRequest) task).getTaskDetails().getResourceId();

        SGLFilesPayload sglFilesPayload = SGLFilesPayload.builder()
                .guid(resourceId)
                .build();
        SGLPayload sglPayload = null;

        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            sglFilesPayload.setPath(((SGLGenericTaskRequest) task).getTaskDetails().getPath());
            sglFilesPayload.setFilename(((SGLGenericTaskRequest) task).getTaskDetails().getFilename());

            sglPayload = SGLPayload.builder()
                    .caller(task.getCorrelationId())
                    .displayName(resourceId)
                    .priority(task.getPriority())
                    .files(List.of(sglFilesPayload))
                    .build();
        } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {
            String path = ((SGLGenericTaskRequest) task).getTaskDetails().getPath();
            if (path != null && !path.isBlank()) {
                if (!path.trim().endsWith("\\")) {
                    path = path.trim().concat("\\");
                }
            }
            String fileName = ((SGLGenericTaskRequest) task).getTaskDetails().getFilename();
            String fullFileName = (path == null) ? "" : path + fileName;
            sglFilesPayload.setFullFileName(fullFileName);

            sglPayload = SGLPayload.builder()
                    .caller(task.getCorrelationId())
                    .displayName(resourceId)
                    .priority(task.getPriority())
                    .target(((SGLGenericTaskRequest) task).getTaskDetails().getLocatorInfo())
                    .deleteFiles(((SGLGenericTaskRequest) task).getTaskDetails().getDeleteSource())
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
            ((SGLGenericTaskRequest) task).getTaskDetails().setDetails(response);

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
        String resourceId = ((SGLGenericTaskRequest) task).getTaskDetails().getResourceId();

        if (StringUtils.isBlank(resourceId)) {
            requiredData = requiredData.concat(", ResourceId");
        }

        // Validate Path
        String path = ((SGLGenericTaskRequest) task).getTaskDetails().getPath();
        if (StringUtils.isBlank(path)) {
            requiredData = requiredData.concat(", Path");
        }

        // Validate Filename
        String filename = ((SGLGenericTaskRequest) task).getTaskDetails().getFilename();
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
            String locatorInfo = ((SGLGenericTaskRequest) task).getTaskDetails().getLocatorInfo();

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

            SGLStatusResponse sglStatusResponse = om.readValue(response, SGLStatusResponse.class);
            Job job = sglStatusResponse.getJob();

            String exitStateMssg = null;
            String queuedStateMssg = null;
            if (job != null) {
                exitStateMssg = job.getExitStateMessage();
                queuedStateMssg = job.getQueuedStateMessage();
            }

            if (exitStateMssg == null || queuedStateMssg == null) {
                taskStatus = TaskStatus.COMPLETED_FAILED;
            } else if ("LiVE".equalsIgnoreCase(exitStateMssg) &&
                    "Running".equalsIgnoreCase(queuedStateMssg)) {
                taskStatus = TaskStatus.IN_PROGRESS;
            } else if ("PASSED".equalsIgnoreCase(exitStateMssg) &&
                    "Finished".equalsIgnoreCase(queuedStateMssg)) {
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
