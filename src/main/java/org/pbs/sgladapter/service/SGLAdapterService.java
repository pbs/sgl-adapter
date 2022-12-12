package org.pbs.sgladapter.service;

import org.pbs.sgladapter.model.FileRestoreTask;
import org.pbs.sgladapter.model.SGLFilesPayload;
import org.pbs.sgladapter.model.SGLPayload;
import org.pbs.sgladapter.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.pbs.sgladapter.model.TaskType.FILE_ARCHIVE;
import static org.pbs.sgladapter.model.TaskType.FILE_RESTORE;

@Service
public class SGLAdapterService implements ISGLAdapterService {
    private Logger logger = LoggerFactory.getLogger(SGLAdapterService.class);

    @Value("${rest.sgl.flashnet.url}")
    private String sglUrl;

    @Override
    public Task createTask(Task task) {
        logger.info("Got task");
        // need to check the type and call the SGL Flashnet WS accordingly
        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {
            String url = sglUrl + "/flashnet/api/v1/files/";


            RestTemplate restTemplate = new RestTemplate();
            SGLFilesPayload sglFilesPayload = SGLFilesPayload.builder()
                    .path(((FileRestoreTask) task).getTaskDetails().getPath())
                    .filename(((FileRestoreTask) task).getTaskDetails().getFilename())
                    .guid(((FileRestoreTask) task).getTaskDetails().getResourceId())
                    .build();

            SGLPayload sglPayload = SGLPayload.builder()
                    .caller(task.getCorrelationId())
                    .displayName(((FileRestoreTask) task).getTaskDetails().getResourceId())
                    .priority(task.getPriority())
                    .files(sglFilesPayload)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType();

            HttpEntity<SGLPayload> entity = new HttpEntity(sglPayload, headers);

            //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            ResponseEntity responseEntity = restTemplate.postForEntity(url, entity, String.class);
            System.out.println(responseEntity.toString());

        }
        return task;
    }

    @Override
    public Task getJobStatus(String taskType, String taskId) {
        Task task = null;

        if (FILE_RESTORE.getType().equalsIgnoreCase(taskType)) {
            task = new FileRestoreTask();

            // need to call SGL status ws


        } else if (FILE_ARCHIVE.getType().equalsIgnoreCase(taskType)) {
            // handle file archive
        }

        return task;
    }
}


