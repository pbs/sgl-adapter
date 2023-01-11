package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pbs.sgladapter.adapter.SglAdapterClient;
import org.pbs.sgladapter.exception.BadRequestException;
import org.pbs.sgladapter.exception.JobNotFoundException;
import org.pbs.sgladapter.exception.ServiceUnavailableException;
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
    public SglGenericRequest createRestoreRequest(SglGenericRequest genericRequest) throws JsonProcessingException {

        String request = prepareCreateRequestForRestore(genericRequest);

        String response = sglAdapterClient.restore(request);

        genericRequest = prepareCreateTaskResponse(response, genericRequest);

        return genericRequest;
    }

    @Override
    public SglGenericRequest createArchiveRequest(SglGenericRequest genericRequest) throws JsonProcessingException {

        String request = prepareCreateRequestForArchive(genericRequest);

        String response = sglAdapterClient.archive(request);

        genericRequest = prepareCreateTaskResponse(response, genericRequest);

        return genericRequest;
    }

    private String prepareCreateRequestForRestore(SglGenericRequest genericRequest) {
        String request = null;

        SglFilesPayload sglFilesPayload = SglFilesPayload.builder()
                .guid(genericRequest.getResourceId())
                .path(genericRequest.getPath())
                .filename(genericRequest.getFilename())
                .build();

        SglPayload sglPayload = SglPayload.builder()
                .caller(genericRequest.getCorrelationId())
                .displayName(genericRequest.getResourceId())
                .priority(genericRequest.getPriority())
                .files(List.of(sglFilesPayload))
                .build();

        ObjectMapper om = new ObjectMapper();
        try {
            request = om.writeValueAsString(sglPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    private String prepareCreateRequestForArchive(SglGenericRequest genericRequest) {
        String request = null;

        SglFilesPayload sglFilesPayload = SglFilesPayload.builder()
                .guid(genericRequest.getResourceId())
                .fullFileName(genericRequest.getFullName())
                .build();

        SglPayload sglPayload = SglPayload.builder()
                .caller(genericRequest.getCorrelationId())
                .displayName(genericRequest.getResourceId())
                .priority(genericRequest.getPriority())
                .target(genericRequest.getLocatorInfo())
                .deleteFiles(genericRequest.getDeleteSource())
                .files(List.of(sglFilesPayload))
                .build();

        ObjectMapper om = new ObjectMapper();
        try {
            request = om.writeValueAsString(sglPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    private SglGenericRequest prepareCreateTaskResponse(String response, SglGenericRequest genericRequest) {

        // throw an exception - empty response
        if (StringUtils.isBlank(response)) {
            throw new ServiceUnavailableException("Unable to get a response from the server");
        }

        ObjectMapper jsonMapper = new JsonMapper();
        JsonNode json = null;
        try {
            json = jsonMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new ServiceUnavailableException("Error while parsing response " + e.getMessage());
        }

        // try to get JobId from the response
        String taskId = json.get("RID").asText();
        int jobId;
        try {
            jobId = Integer.parseInt(taskId);
        } catch (NumberFormatException e) {
            throw new BadRequestException(response);
        }

        if (jobId <= 0) {
            throw new BadRequestException(response);
        }

        genericRequest.setTaskId(taskId);
        genericRequest.setStatus(TaskStatus.COMPLETED_SUCCESS);
        genericRequest.setDetails(response);


        return genericRequest;
    }

    @Override
    public SglGenericRequest getJobStatus(String taskId) {
        logger.info("inside of Service.getJobStatus");

        String response = sglAdapterClient.getTaskStatus(taskId);

        logger.info(response);

        SglGenericRequest taskStatusResponse = convertStatusResponse(response, taskId);

        return taskStatusResponse;
    }

    private SglGenericRequest convertStatusResponse(String response, String taskId) {

        if (StringUtils.isBlank(response)) {
            throw new ServiceUnavailableException("Unable to get a response from the server");
        }

        SglGenericRequest taskStatusResponse = new SglGenericRequest();

        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

        try {
            ObjectMapper om = new ObjectMapper();

            SglStatusResponse sglStatusResponse = om.readValue(response, SglStatusResponse.class);
            Job job = sglStatusResponse.getJob();

            taskStatusResponse.setDetails(response);


            int exitState = 1;
            if (job != null) {
                exitState = job.getExitState();

                taskStatusResponse.setCorrelationId(job.getCallingProduct());
                taskStatusResponse.setResourceId(job.getDisplayName());
            }

            //The finished status of the job. One of the following values:
            // 0 (NO_SUCH_JOB)
            // 1 (LIVE)
            // 2 (KILLED)
            // 3 (STOPPED)
            // 4 (FAILED)
            // 5 (PASSED)
            // +6 (PASSED_WITH_WARNING
            if (exitState == 0) {
                throw new JobNotFoundException(taskId);
            } else if (exitState == 1) {
                taskStatus = TaskStatus.IN_PROGRESS;
            } else if (exitState >= 5) {
                taskStatus = TaskStatus.COMPLETED_SUCCESS;
            } else {
                taskStatus = TaskStatus.COMPLETED_FAILED;

                // handle error
                taskStatusResponse.getError().put("description", job.getQueuedStateMessage());
                taskStatusResponse.getError().put("detail", response);
            }
        } catch (JsonProcessingException e) {
            throw new ServiceUnavailableException("Error while parsing response for getStatus " + e.getMessage());
        }

        taskStatusResponse.setStatus(taskStatus);

        return taskStatusResponse;
    }
}
