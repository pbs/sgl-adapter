package org.pbs.sgladapter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbs.sgladapter.adapter.SglAdapterClient;
import org.pbs.sgladapter.exception.BadRequestException;
import org.pbs.sgladapter.exception.JobNotFoundException;
import org.pbs.sgladapter.exception.ServiceUnavailableException;
import org.pbs.sgladapter.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class SglAdapterServiceImplTest {


  private static final Logger logger = LoggerFactory.getLogger(SglAdapterServiceImplTest.class);

  @InjectMocks
  private SglAdapterServiceImpl sglAdapterServiceImpl;

  @Mock
  private SglAdapterClient mockSglAdapterClient;

  private static SglGenericRequest.SglGenericRequestBuilder buildBaseSglGenericRequestBuilder() {
    return SglGenericRequest.builder()
            .correlationId("123e4567-e89h-12d3-a456-9AC7CBDCEE52")
            .priority(2)
            .path("\\\\m-isilonsmb\\gpop_dev\\mxf")
            .resourceId("P123123-001")
            .filename("P123123-001.mxf");
  }


  @Test
  public void testCreateTaskSuccess() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    when(mockSglAdapterClient.restore(any(String.class)))
            .thenReturn("""
                    {
                      "Files": {},
                      "totalEntriesProcessed": 0,
                      "Success": true,
                      "Errors": [],
                      "RID": 1336,
                      "Message": "Restoring 1 full file",
                      "Lid": "26102022-af331005ae2a44a9ab19f8ed401ffee8"
                    }
                    """);

    try {
      inputTask = sglAdapterServiceImpl.createRestoreRequest(inputTask);

      assertEquals(inputTask.getTaskId(), "1336");

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void testCreateTaskSuccessArchive() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    inputTask.setLocatorInfo("lab_mxf_D");

    when(mockSglAdapterClient.archive(any(String.class)))
        .thenReturn("""
          {
            "Files": {},
            "Folders": {},
            "Success": true,
            "Errors": [],
            "RID": 1417,
            "UAN": "4EF8E6F8-B7C0-45B5-A211-EE88DCA2DE14",
            "Message": "Successfully sent to archive as request id 1417",
            "Lid": "16122022-7d62c1aa80cd41549313618fd0eed0b2"
          }
          """);

    try {
      inputTask = sglAdapterServiceImpl.createArchiveRequest(inputTask);

      assertEquals(inputTask.getTaskId(), "1417");

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void testCreateRestoreFailed() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    when(mockSglAdapterClient.restore(any(String.class)))
            .thenReturn("""
                    {
                      "Files": {},
                      "totalEntriesProcessed": 0,
                      "Success": false,
                      "Errors": [
                        "The request was rejected."
                      ],
                      "RID": 0,
                      "Message": "The request was rejected.",
                      "Lid": "15122022-b14be94ee6a3498490f16ee3f46209db"
                    }
                    """);

    Assertions.assertThrows(BadRequestException.class, () -> {
      sglAdapterServiceImpl.createRestoreRequest(inputTask);
    });
  }

  @Test
  public void testCreateRestoreEmptyResponse() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    when(mockSglAdapterClient.restore(any(String.class)))
            .thenReturn("");

    Assertions.assertThrows(ServiceUnavailableException.class, () -> {
      sglAdapterServiceImpl.createRestoreRequest(inputTask);
    });
  }


  @Test
  public void testCreateArchiveFailed() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    when(mockSglAdapterClient.archive(any(String.class)))
            .thenReturn("""
                    {
                      "Files": {},
                      "totalEntriesProcessed": 0,
                      "Success": false,
                      "Errors": [
                        "The request was rejected."
                      ],
                      "RID": 0,
                      "Message": "The request was rejected.",
                      "Lid": "15122022-b14be94ee6a3498490f16ee3f46209db"
                    }
                    """);

    Assertions.assertThrows(BadRequestException.class, () -> {
      sglAdapterServiceImpl.createArchiveRequest(inputTask);
    });
  }

  @Test
  public void testCreateArchiveEmptyResponse() {
    // Create a Task to be passed into the TaskService's createTask method.
    SglGenericRequest inputTask = buildBaseSglGenericRequestBuilder().build();

    when(mockSglAdapterClient.archive(any(String.class)))
            .thenReturn("");

    Assertions.assertThrows(ServiceUnavailableException.class, () -> {
      sglAdapterServiceImpl.createArchiveRequest(inputTask);
    });
  }


  @Test
  public void testGetTaskSuccess() {
    String taskId = "1377";
    when(mockSglAdapterClient.getTaskStatus(any(String.class)))
            .thenReturn("""
                    {
                      "Job": {
                        "RID": 1377,
              "RunState": 1,
              "ExitState": 5,
              "ExitStateMessage": "PASSED",
              "QueuedStateCode": 0,
              "QueuedStateMessage": "Finished",
              "Action": 2,
              "Status": 0,
              "DisplayName": "P222222-555",
              "Priority": 3,
              "ExecutingServer": "M-MTDM0AP-LAB",
              "Group": "013272L6",
              "Volume": "013272L6",
              "Changer": "Lab_Quantum",
              "CallingProduct": "123e4567-e89b-12d3-a456-9AC7CBDCEE69",
              "CallingServer": "M-MTSC0AP-LAB",
              "LFK": 1896,
              "QK": 0,
              "Size": 19614483,
              "BytesTransferred": 19614483,
              "BytesVerified": 0,
              "WillVerify": false,
              "Percent": 100
            },
            "Success": true,
            "Errors": [],
            "Message": "Current queue status of job 1377",
            "Lid": "15122022-715e4e7ba4ae464db04eefaf944ebc28"
          }
          """);

    SglGenericRequest response = sglAdapterServiceImpl.getJobStatus(taskId);

    assertEquals(response.getStatus(), TaskStatus.COMPLETED_SUCCESS);

  }

  @Test
  public void testGetTaskInvalidTaskId() {
    String taskId = "13770";
    when(mockSglAdapterClient.getTaskStatus(any(String.class)))
        .thenReturn("""
          {
            "Job": {
              "RID": 0,
              "RunState": 0,
              "ExitState": 0,
              "ExitStateMessage": null,
              "QueuedStateCode": -999,
              "QueuedStateMessage": "No such job",
              "Action": 0,
              "Status": 0,
              "DisplayName": null,
              "Priority": 0,
              "ExecutingServer": null,
              "Group": null,
              "Volume": null,
              "Changer": null,
              "CallingProduct": null,
              "CallingServer": null,
              "LFK": 0,
              "QK": 0,
              "Size": 0,
              "BytesTransferred": 0,
              "BytesVerified": 0,
                    "WillVerify": false,
                    "Percent": 0
                  },
                  "Success": false,
                  "Errors": [],
                  "Message": null,
                  "Lid": "15122022-5524170bbf1d4df780b31d7cf6806611"
                }
                """);

    Assertions.assertThrows(JobNotFoundException.class, () -> {
      sglAdapterServiceImpl.getJobStatus(taskId);
    });
  }


  public void testGetTaskEmptyResponse() {
    String taskId = "13770";
    when(mockSglAdapterClient.getTaskStatus(any(String.class)))
            .thenReturn("");

    Assertions.assertThrows(ServiceUnavailableException.class, () -> {
      sglAdapterServiceImpl.getJobStatus(taskId);
    });
  }

}
