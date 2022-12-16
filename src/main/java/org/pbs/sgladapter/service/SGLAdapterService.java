package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.model.SGLGenericTaskRequest;
import org.pbs.sgladapter.model.SGLGenericTaskResponse;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatus;
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
    public Task createTask(Task task) throws JsonProcessingException {
        logger.info("inside of Service.createTask");
        String response = "";
        String request = prepareCreateTaskRequest(task);
        logger.info("Type is : {}", task.getType());
        logger.info("Request is : {}", request);
        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            response = sglAdapterClient.restore(request);
        }
        else if (FILE_ARCHIVE.getType().equalsIgnoreCase(task.getType())) {
            response = sglAdapterClient.archive(request);
        }
        logger.info(response);

        task = prepareCreateTaskResponse(response, task);

        return task;
    }

    private String prepareCreateTaskRequest(Task task) {

        SGLFilesPayload sglFilesPayload = SGLFilesPayload.builder()
                .guid(((SGLGenericTaskRequest) task).getTaskDetails().getResourceId())
                .build();
        SGLPayload sglPayload = null;

        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            sglFilesPayload.setPath(((SGLGenericTaskRequest) task).getTaskDetails().getPath());
            sglFilesPayload.setFilename(((SGLGenericTaskRequest) task).getTaskDetails().getFilename());

            sglPayload = SGLPayload.builder()
                    .caller(task.getCorrelationId())
                    .displayName(((SGLGenericTaskRequest) task).getTaskDetails().getResourceId())
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
                    .displayName(((SGLGenericTaskRequest) task).getTaskDetails().getResourceId())
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
        if (!response.isEmpty() && !response.isBlank()) {
            ObjectMapper jsonMapper = new JsonMapper();
            JsonNode json = null;
            try {
                json = jsonMapper.readTree(response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String taskId = json.get("RID").asText();
            if (Integer.parseInt(taskId) <= 0) {
                logger.error("Failed to create a new task for " + task.getType()
                        + " [" + response + "]");
            }
            task.setTaskId(taskId);
            ((SGLGenericTaskRequest) task).getTaskDetails().setDetails(response);

        }

        return task;
    }

    @Override
    public Task getJobStatus(String taskType, String taskId) {
        logger.info("inside of Service.getJobStatus");

        String response = sglAdapterClient.getTaskStatus(taskId);

        System.out.println(response);

        Task responseTask = new SGLGenericTaskResponse();

        responseTask.setType(taskType);
        responseTask.setTaskId(taskId);

        try {
            ObjectMapper om = new ObjectMapper();

            SGLStatusResponse sglStatusResponse = om.readValue(response, SGLStatusResponse.class);
            Job job = sglStatusResponse.getJob();

            String exitStateMssg = job.getExitStateMessage();
            String queuedStateMssg = job.getQueuedStateMessage();

            TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

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

            responseTask.setStatus(taskStatus);

            ((SGLGenericTaskResponse) responseTask).setTaskDetails(sglStatusResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return responseTask;
    }
}
