package org.pbs.sgladapter.adapter;

import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.model.sgl.SGLPayload;
import org.pbs.sgladapter.model.sgl.SGLStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAdapterClient {

  @PostMapping("/v1/files")
  public Task restore(@RequestBody SGLPayload payload);

  @PostMapping("/v2/assets")
  public Task archive(@RequestBody SGLPayload payload);

  @GetMapping("/v2/jobs/{taskId}")
  public SGLStatusResponse getTaskStatus(@PathVariable String taskId);
}
