package org.pbs.sgladapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.service.ISGLAdapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
@Tag(description = "Provides access to Tasks",
        name = "Task Resource")
public class SGLAdapterController {

    private ISGLAdapterService sglAdapterService;

    public SGLAdapterController(ISGLAdapterService sglAdapterService) {
        this.sglAdapterService = sglAdapterService;
    }

    private static final Logger logger = LoggerFactory.getLogger(SGLAdapterController.class);

    @PostMapping("/task")

    public ResponseEntity<Task> createTask(@RequestBody Task task) throws JsonProcessingException {
        logger.info("Task received {}", task);
        Task retTask = sglAdapterService.createTask(task);
        return new ResponseEntity<>(retTask, HttpStatus.OK);
    }

    @GetMapping("/task/{taskType}/{taskId}")
    public ResponseEntity<Task> getJobStatusForTaskId(@PathVariable String taskType,
                                                      @PathVariable String taskId) {
        logger.info("Getting job status for taskId:" + taskType + " - " + taskId);
        Task task = sglAdapterService.getJobStatus(taskType, taskId);
        logger.info("Got task:" + task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

}
