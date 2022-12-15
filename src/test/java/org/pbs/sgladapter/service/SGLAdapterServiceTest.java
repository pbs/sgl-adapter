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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    public void testCreateTaskSuccess() {
        // Create a Task to be passed into the TaskService's createTask method.
        SGLGenericTaskDetailsRequest taskInputDetails =
                SGLGenericTaskDetailsRequest.builder().path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                        .resourceId("P123123-001").filename("P123123-001.mxf").build();
        SGLGenericTaskRequest inputTask = SGLGenericTaskRequest.builder().type("FileRestore")
                .correlationId("123e4567-e89h-12d3-a456-9AC7CBDCEE52").
                priority(2).taskDetails(taskInputDetails).build();

        when(mockSglAdapterClient.restore(any(String.class)))
                .thenReturn("{\"Files\":{},\"totalEntriesProcessed\":0,\"Success\":true,\"Errors\":[],\"RID\":1336,\"Message\":\"Restoring 1 full file\",\"Lid\":\"26102022-af331005ae2a44a9ab19f8ed401ffee8\"}");

        try {
            Task response = sglAdapterService.createTask(inputTask);

            assertEquals(response.getTaskId(), "1336");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
