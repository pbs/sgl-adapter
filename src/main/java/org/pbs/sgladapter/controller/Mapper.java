package org.pbs.sgladapter.controller;

import org.pbs.sgladapter.dto.FileArchiveDto;
import org.pbs.sgladapter.dto.FileRestoreDto;
import org.pbs.sgladapter.dto.CreateResponseDto;
import org.pbs.sgladapter.dto.StatusResponseDto;
import org.pbs.sgladapter.model.SglGenericRequest;
import org.springframework.stereotype.Component;


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

    public SglGenericRequest toGenericRequest(FileArchiveDto fileArchiveDto) {
        return SglGenericRequest.builder()
                .correlationId(fileArchiveDto.getCorrelationId())
                .type(fileArchiveDto.getType())
                .priority(fileArchiveDto.getPriority())
                .path(fileArchiveDto.getTaskDetails().getPath())
                .filename(fileArchiveDto.getTaskDetails().getFilename())
                .resourceId(fileArchiveDto.getTaskDetails().getResourceId())
                .locatorInfo(fileArchiveDto.getTaskDetails().getLocatorInfo())
                .deleteSource(fileArchiveDto.getTaskDetails().getDeleteSource())
                .build();
    }

    public CreateResponseDto toFileRestoreResponse(SglGenericRequest genericRequest) {
        return CreateResponseDto.builder()
                .taskId(genericRequest.getTaskId())
                .correlationId(genericRequest.getCorrelationId())
                .type(genericRequest.getType())
                .priority(genericRequest.getPriority())
                .taskDetails(genericRequest.getTaskDetailsForRestore())
                .status(genericRequest.getStatus())
                .build();
    }

    public CreateResponseDto toFileArchiveResponse(SglGenericRequest genericRequest) {
        return CreateResponseDto.builder()
                .taskId(genericRequest.getTaskId())
                .correlationId(genericRequest.getCorrelationId())
                .type(genericRequest.getType())
                .priority(genericRequest.getPriority())
                .taskDetails(genericRequest.getTaskDetailsForArchive())
                .status(genericRequest.getStatus())
                .build();
    }


    public StatusResponseDto toStatusResponseDto(SglGenericRequest genericRequest) {
        return StatusResponseDto.builder()
                .requestorCorrelationId(genericRequest.getCorrelationId())
                .status(genericRequest.getStatus())
                .resourceId(genericRequest.getResourceId())
                .timestamp(genericRequest.getTimestamp().toString())
                .error(genericRequest.getError())
                .responseDetails(genericRequest.getDetails())
                .build();
    }


}
