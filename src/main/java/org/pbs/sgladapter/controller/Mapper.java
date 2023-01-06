package org.pbs.sgladapter.controller;

import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.FileRestoreResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
public class Mapper {

    public SglGenericRequest toGenericRequest(FileRestoreDto fileRestoreDto) {
        return SglGenericRequest.builder()
                .correlationId(fileRestoreDto.getCorrelationId())
                .type(fileRestoreDto.getType())
                .priority(fileRestoreDto.getPriority())
                .path(fileRestoreDto.getTaskDetails().getPath())
                .filename(fileRestoreDto.getTaskDetails().getFilename())
                .resourceId(fileRestoreDto.getTaskDetails().getResourceId())
                .build();
    }

    public FileRestoreResponseDto toFileRestoreResponse(SglGenericRequest request) {

        Map<String, String> taskDetails = new HashMap();

        taskDetails.put("path", request.getPath());
        taskDetails.put("filename", request.getFilename());
        taskDetails.put("resourceId", request.getResourceId());
        taskDetails.put("details", request.getDetails());

        return FileRestoreResponseDto.builder()
                .taskId(request.getTaskId())
                .correlationId(request.getCorrelationId())
                .type(request.getType())
                .priority(request.getPriority())
                .taskDetails(taskDetails)
                .status(request.getStatus())
                .build();
    }
}
