package org.pbs.sgladapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.pbs.sgladapter.model.Task;
import org.pbs.sgladapter.service.ISGLAdapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void setSglAdapterService(ISGLAdapterService sglAdapterService) {
        this.sglAdapterService = sglAdapterService;
    }

    private static final Logger logger = LoggerFactory.getLogger(SGLAdapterController.class);

    @PostMapping("/task")
    //@Operation(summary = "Create task",
    //        description = "Provides capability to submit tasks for submission and monitoring")
/*    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "${api.response-codes.created.desc}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.response-codes.badRequest.desc}",
                    content = {@Content(examples = {@ExampleObject(value = "")})}),
            @ApiResponse(responseCode = "422",
                    description = "${api.response-codes.unprocessableEntity.desc}",
                    content = {@Content(examples = {@ExampleObject(value = "")})})})*/

    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Task received {}", task);
        sglAdapterService.createTask(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    /*@Operation(summary = "Get a SGL JobStatus for a given taskId",
            description = "Provides capability to get a SGL job status for a given taskId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.response-codes.ok.desc}"),
            @ApiResponse(responseCode = "404",
                    description = "${api.response-codes.notFound.desc}",
                    content = {@Content(examples = {@ExampleObject(value = "")})})})

     */
    public ResponseEntity<Task> getJobStatusForTaskId(@PathVariable String taskId) {
        logger.info("Getting job status for taskId:" + taskId);
        Task task = sglAdapterService.getJobStatus(taskId);
        logger.info("Got task:" + task);
        return new ResponseEntity<>(task, HttpStatus.OK);

    }

}
