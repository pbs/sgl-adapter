package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public Task getJobStatus(String tasktype, String taskId) {
        logger.info("inside of Service.getJobStatus");
        return null;
    }
}
