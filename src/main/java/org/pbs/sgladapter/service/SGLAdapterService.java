package org.pbs.sgladapter.service;

import org.pbs.sgladapter.model.FileRestoreTask;
import org.pbs.sgladapter.model.SGLFilesPayload;
import org.pbs.sgladapter.model.SGLPayload;
import org.pbs.sgladapter.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SGLAdapterService implements ISGLAdapterService {
    private Logger logger = LoggerFactory.getLogger(SGLAdapterService.class);

    @Override
    public Task createTask(Task task) {
        logger.info("Got task");
        // need to check the type and call the SGL Flashnet WS accordingly
        if (task.getType().equals("FileRestore")) {
            String url = "http://m-mtsc0ap-lab.hq.corp.pbs.org:11000/flashnet/api/v1/files/";


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
    public Task getJobStatus(String taskId) {
        Task task = new FileRestoreTask();

        // need to call SGL status ws
        // currently there's no way to know if the job is FileRestore vs FileArchive
        // I think when we send "caller" in the createTask, it needs to append type
        // and here, we'll know which task object to create.
        // Or ... we can create a generic SGLTask object??

        return task;
        //return task;
    }
}


