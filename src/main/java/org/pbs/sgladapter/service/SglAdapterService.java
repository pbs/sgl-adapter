package org.pbs.sgladapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pbs.sgladapter.model.SglGenericRequest;

public interface SglAdapterService {

  public SglGenericRequest createRestoreRequest(SglGenericRequest genericRequest);

  public SglGenericRequest createArchiveRequest(SglGenericRequest genericRequest);

  public SglGenericRequest getJobStatus(String taskId);
}
