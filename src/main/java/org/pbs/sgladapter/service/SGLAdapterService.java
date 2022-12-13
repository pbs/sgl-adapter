package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pbs.sgladapter.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

@Service
public class SGLAdapterService implements ISGLAdapterService {
    private Logger logger = LoggerFactory.getLogger(SGLAdapterService.class);


    private String sglUrl;

    @Override
    public Task createTask(Task task) throws JsonProcessingException {
        logger.info("Got task");
        // need to check the type and call the SGL Flashnet WS accordingly
        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            //String url = "http://m-mtsc0ap-lab.hq.corp.pbs.org:11000" + "/flashnet/api/v1/files/";
            String url = sglUrl + "/flashnet/api/v1/files/";


            RestTemplate restTemplate = new RestTemplate();
            SGLFilesPayload sglFilesPayload = SGLFilesPayload.builder()
                    .path(((FileRestoreTaskRequest) task).getTaskDetails().getPath())
                    .filename(((FileRestoreTaskRequest) task).getTaskDetails().getFilename())
                    .guid(((FileRestoreTaskRequest) task).getTaskDetails().getResourceId())
                    .build();

            SGLPayload sglPayload = SGLPayload.builder()
                    .caller(task.getCorrelationId())
                    .displayName(((FileRestoreTaskRequest) task).getTaskDetails().getResourceId())
                    .priority(task.getPriority())
                    .files(List.of(sglFilesPayload))
                    .build();

            ObjectMapper om = new ObjectMapper();

            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("text/plain");
            headers.setContentType(mediaType);

            HttpEntity<String> entity = new HttpEntity(om.writeValueAsString(sglPayload), headers);

            String responseEntity = restTemplate.postForObject(url, entity, String.class);
            //System.out.println(responseEntity);

            ObjectMapper mapper = new JsonMapper();
            JsonNode json = mapper.readTree(responseEntity);

            String taskId = json.get("RID").asText();
            task.setTaskId(taskId);

        }
        return task;
    }

    @Override
    public Task getJobStatus(String taskType, String taskId) {
        Task task = null;

        if (FILE_RESTORE.getType().equalsIgnoreCase(taskType)) {
            task = new FileRestoreTaskResponse();

            task.setType(taskType);
            task.setTaskId(taskId);

            // need to call SGL status ws
            //String url = "" + "/flashnet/api/v2/jobs/" + taskId;
            String url = sglUrl + "/flashnet/api/v2/jobs/" + taskId;

            RestTemplate restTemplate = new RestTemplate();


            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println(response.getBody());

            ObjectMapper om = new ObjectMapper();
            try {
                SGLStatusResponse sglStatusResponse = om.readValue(response.getBody(), SGLStatusResponse.class);
                Job job = sglStatusResponse.getJob();
                TaskStatus taskStatus = TaskStatus.IN_PROGRESS;
                String exitStateMssg = job.getExitStateMessage();
                String queuedStateMssg = job.getQueuedStateMessage();

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

                task.setStatus(taskStatus);

                ((FileRestoreTaskResponse) task).setTaskDetails(sglStatusResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(taskType)) {
            // handle file archive
        }

        return task;
    }
}


