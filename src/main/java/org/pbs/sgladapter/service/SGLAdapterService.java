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

/*    @Value("${rest.sgl.flashnet.url}")
    private String sglUrl;*/

    @Override
    public Task createTask(Task task) throws JsonProcessingException {
        logger.info("Got task");
        // need to check the type and call the SGL Flashnet WS accordingly
        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            String url = "http://m-mtsc0ap-lab.hq.corp.pbs.org:11000" + "/flashnet/api/v1/files/";


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

/*            String payload = "{\n" +
                    " \"Caller\":\"Postman Test (adhoc) - bpmuatpu\",\n" +
                    " \"DisplayName\":\"P222222-555\",\n" +
                    " \"Priority\":60,\n" +
                    " \"Files\":[\n" +
                    " {\n" +
                    " \"Guid\":\"P222222-555\",\n" +
                    " \"Path\":\"\\\\\\\\m-isilonsmb\\\\gpop_dev\\\\mxf\",\n" +
                    " \"Filename\":\"P222222-555.mxf\"\n" +
                    " }\n" +
                    " ]\n" +
                    "}";*/

            System.out.println("payload is :");
            System.out.println(om.writeValueAsString(sglPayload));
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("text/plain");
            headers.setContentType(mediaType);

            //HttpEntity<String> entity = new HttpEntity("{"+sglPayload+"}", headers);
            HttpEntity<String> entity = new HttpEntity(om.writeValueAsString(sglPayload), headers);

            //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            String responseEntity = restTemplate.postForObject(url, entity, String.class);
            //String responseEntity = restTemplate.postForObject(url, entity, String.class);
            System.out.println(responseEntity);

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

            // need to call SGL status ws
            String url = "http://m-mtsc0ap-lab.hq.corp.pbs.org:11000" + "/flashnet/api/v2/jobs/" + taskId;

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.println(response.toString());

        } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(taskType)) {
            // handle file archive
        }

        return task;
    }
}


