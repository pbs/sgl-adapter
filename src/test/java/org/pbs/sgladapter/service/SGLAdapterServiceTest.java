package org.pbs.sgladapter.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.pbs.sgladapter.model.FileRestoreTaskDetailsRequest;
import org.pbs.sgladapter.model.FileRestoreTaskRequest;
import org.pbs.sgladapter.model.SGLFilesPayloadTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class SGLAdapterServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SGLAdapterServiceTest.class);

    private SGLAdapterService sglAdapterService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testCreateTaskSuccess() {
        // Create a Task to be passed into the TaskService's createTask method.
        FileRestoreTaskDetailsRequest taskInputDetails =
                FileRestoreTaskDetailsRequest.builder().path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                        .resourceId("P123123-001").filename("P123123-001.mxf").build();
        FileRestoreTaskRequest inputTask = FileRestoreTaskRequest.builder().type("FileRestore")
                .correlationId("123e4567-e89h-12d3-a456-9AC7CBDCEE52").
                priority(2).taskDetails(taskInputDetails).build();

        String url = "";

        //when(restTemplate.postForObject(url, entity, String.class)).thenReturn(inputTask);


    }

}
