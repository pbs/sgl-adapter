package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.model.SGLGenericTaskRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.sgl.SGLFilesPayload;
import org.pbs.sgladapter.model.sgl.SGLPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (FILE_RESTORE.getType().equalsIgnoreCase(task.getType())) {

            String response = sglAdapterClient.restore(sglPayload);

            System.out.println(response);

        } else {

        }
        return null;
    }

    @Override
    public Task getJobStatus(String tasktype, String taskId) {
        logger.info("inside of Service.getJobStatus");

        String response = sglAdapterClient.getTaskStatus(taskId);

        System.out.println(response);

        return null;
    }
}
