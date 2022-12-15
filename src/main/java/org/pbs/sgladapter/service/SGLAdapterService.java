package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.model.SGLGenericTaskRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.sgl.SGLFilesPayload;
import org.pbs.sgladapter.model.sgl.SGLPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {

            String request = prepareCreateTaskRequest(task);
            System.out.println("Request = " + request);

            response = sglAdapterClient.restore(request);

            System.out.println(response);


        } else {

        }

        task = prepareCreateTaskResponse(response, task);

        return task;
    }

    private String prepareCreateTaskRequest(Task task) {

        SGLFilesPayload sglFilesPayload = SGLFilesPayload.builder()
                .guid(((SGLGenericTaskRequest) task).getTaskDetails().getResourceId())
                .build();

        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            sglFilesPayload.setPath(((SGLGenericTaskRequest) task).getTaskDetails().getPath());
            sglFilesPayload.setFilename(((SGLGenericTaskRequest) task).getTaskDetails().getFilename());
        } else {
            // FileArchive
            // TBD
        }

        SGLPayload sglPayload = SGLPayload.builder()
                .caller(task.getCorrelationId())
                .displayName(((SGLGenericTaskRequest) task).getTaskDetails().getResourceId())
                .priority(task.getPriority())
                .files(List.of(sglFilesPayload))
                .build();

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
        }

        return task;
    }

    @Override
    public Task getJobStatus(String tasktype, String taskId) {
        logger.info("inside of Service.getJobStatus");

        String response = sglAdapterClient.getTaskStatus(taskId);

        System.out.println(response);

        return null;
    }
}
