package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.adapter.SGLAdapterClient;
import org.pbs.sgladapter.model.SGLGenericTaskDetailsRequest;
import org.pbs.sgladapter.model.SGLGenericTaskRequest;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SGLAdapterServiceTest {


    private static final Logger logger = LoggerFactory.getLogger(SGLAdapterServiceTest.class);

    @InjectMocks
    private SGLAdapterService sglAdapterService;

    @Mock
    private SGLAdapterClient mockSglAdapterClient;

    private static SGLGenericTaskRequest.SGLGenericTaskRequestBuilder buildBaseSGLGenericTaskRequestBuilder() {

        // Create a Task to be passed into the TaskService's createTask method.
        SGLGenericTaskDetailsRequest taskInputDetails =
                SGLGenericTaskDetailsRequest.builder().path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                        .resourceId("P123123-001").filename("P123123-001.mxf").build();

        return SGLGenericTaskRequest.builder().type("FileRestore")
                .correlationId("123e4567-e89h-12d3-a456-9AC7CBDCEE52").
                priority(2).taskDetails(taskInputDetails);
    }

    @Test
    public void testCreateTaskSuccess() {
        // Create a Task to be passed into the TaskService's createTask method.
        SGLGenericTaskRequest inputTask = buildBaseSGLGenericTaskRequestBuilder().build();

        when(mockSglAdapterClient.restore(any(String.class)))
                .thenReturn("{\"Files\":{},\"totalEntriesProcessed\":0,\"Success\":true,\"Errors\":[],\"RID\":1336,\"Message\":\"Restoring 1 full file\",\"Lid\":\"26102022-af331005ae2a44a9ab19f8ed401ffee8\"}");

        try {
            Task response = sglAdapterService.createTask(inputTask);

            assertEquals(response.getTaskId(), "1336");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testCreateTaskFailed() {
        // Create a Task to be passed into the TaskService's createTask method.
        SGLGenericTaskRequest inputTask = buildBaseSGLGenericTaskRequestBuilder().build();

        when(mockSglAdapterClient.restore(any(String.class)))
                .thenReturn("{\"Files\":{},\"totalEntriesProcessed\":0,\"Success\":false,\"Errors\":[\"The request was rejected by the FlashNet XML Service. Found none of the requested assets in the archive.\"],\"RID\":0,\"Message\":\"The request was rejected by the FlashNet XML Service. Found none of the requested assets in the archive.\",\"Lid\":\"15122022-b14be94ee6a3498490f16ee3f46209db\"}");

        try {
            Task response = sglAdapterService.createTask(inputTask);

            assertEquals(response.getTaskId(), "0");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testGetTaskSuccess() {
        String taskId = "1377";
        when(mockSglAdapterClient.getTaskStatus(any(String.class)))
                .thenReturn("{\"Job\":{\"RID\":1377,\"RunState\":1,\"ExitState\":5,\"ExitStateMessage\":\"PASSED\",\"QueuedStateCode\":0,\"QueuedStateMessage\":\"Finished\",\"Action\":2,\"Status\":0,\"DisplayName\":\"P222222-555\",\"Priority\":3,\"ExecutingServer\":\"M-MTDM0AP-LAB\",\"Group\":\"013272L6\",\"Volume\":\"013272L6\",\"Changer\":\"Lab_Quantum\",\"CallingProduct\":\"123e4567-e89b-12d3-a456-9AC7CBDCEE69\",\"CallingServer\":\"M-MTSC0AP-LAB\",\"LFK\":1896,\"QK\":0,\"Size\":19614483,\"BytesTransferred\":19614483,\"BytesVerified\":0,\"WillVerify\":false,\"Percent\":100},\"Success\":true,\"Errors\":[],\"Message\":\"Current queue status of job 1377\",\"Lid\":\"15122022-715e4e7ba4ae464db04eefaf944ebc28\"}");

        Task response = sglAdapterService.getJobStatus("FileRestore", taskId);

        assertEquals(response.getStatus(), TaskStatus.COMPLETED_SUCCESS);

    }

    @Test
    public void testGetTaskInvalidTaskId() {
        String taskId = "13770";
        when(mockSglAdapterClient.getTaskStatus(any(String.class)))
                .thenReturn("{\"Job\":{\"RID\":0,\"RunState\":0,\"ExitState\":0,\"ExitStateMessage\":null,\"QueuedStateCode\":-999,\"QueuedStateMessage\":\"No such job\",\"Action\":0,\"Status\":0,\"DisplayName\":null,\"Priority\":0,\"ExecutingServer\":null,\"Group\":null,\"Volume\":null,\"Changer\":null,\"CallingProduct\":null,\"CallingServer\":null,\"LFK\":0,\"QK\":0,\"Size\":0,\"BytesTransferred\":0,\"BytesVerified\":0,\"WillVerify\":false,\"Percent\":0},\"Success\":false,\"Errors\":[],\"Message\":null,\"Lid\":\"15122022-5524170bbf1d4df780b31d7cf6806611\"}");

        Task response = sglAdapterService.getJobStatus("FileRestore", taskId);

        assertEquals(response.getStatus(), TaskStatus.COMPLETED_FAILED);

    }

}
