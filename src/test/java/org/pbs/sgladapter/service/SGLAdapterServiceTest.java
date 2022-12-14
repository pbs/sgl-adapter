package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.model.FileRestoreTaskDetailsRequest;
import org.pbs.sgladapter.model.FileRestoreTaskRequest;
import org.pbs.sgladapter.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = SGLAdapterServiceTest.class)
//@TestPropertySource(locations="classpath:application-dev.properties")
@ActiveProfiles("test")
public class SGLAdapterServiceTest {

    /*
    private static final Logger logger = LoggerFactory.getLogger(SGLAdapterServiceTest.class);

    @InjectMocks
    private OLDSGLAdapterService sglAdapterService;

    @Mock
    private RestTemplate mockRestTemplate;

    @Test
    public void testCreateTaskSuccess() {
        // Create a Task to be passed into the TaskService's createTask method.
        FileRestoreTaskDetailsRequest taskInputDetails =
                FileRestoreTaskDetailsRequest.builder().path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                        .resourceId("P123123-001").filename("P123123-001.mxf").build();
        FileRestoreTaskRequest inputTask = FileRestoreTaskRequest.builder().type("FileRestore")
                .correlationId("123e4567-e89h-12d3-a456-9AC7CBDCEE52").
                priority(2).taskDetails(taskInputDetails).build();

        lenient().when(mockRestTemplate.postForObject(any(String.class).toString(), any(HttpEntity.class), String.class))
                .thenReturn("{\"Files\":{},\"totalEntriesProcessed\":0,\"Success\":true,\"Errors\":[],\"RID\":1336,\"Message\":\"Restoring 1 full file\",\"Lid\":\"26102022-af331005ae2a44a9ab19f8ed401ffee8\"}");

        try {
            Task response = sglAdapterService.createTask(inputTask);

            assertEquals(response.getTaskId(), "1336");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

     */

}
