package org.pbs.sgladapter.adapter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdapterClient {

  @PostMapping("/v1/files/")
  public String restore(@RequestBody String payload);

  @PostMapping("/v2/assets/")
  public String archive(@RequestBody String payload);

  @GetMapping("/v2/jobs/{taskId}")
  public String getTaskStatus(@PathVariable String taskId);
}
