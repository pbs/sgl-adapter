package org.pbs.sgladapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class SglGenericRequest {

    private String correlationId;

    private String taskId;

    private String type;

    private int priority;

    private TaskStatus status;

    private String path;

    private String filename;

    private String resourceId;

    private String locatorInfo;

    private Boolean deleteSource;

    private String details;

    private LocalDateTime timestamp;

    private Map<String, String> error;

    public Map<String, Object> getTaskDetailsForRestore() {
        Map<String, Object> taskDetails = new HashMap();

        taskDetails.put("path", this.getPath());
        taskDetails.put("filename", this.getFilename());
        taskDetails.put("resourceId", this.getResourceId());
        taskDetails.put("details", this.getDetails());

        return taskDetails;
    }


    public Map<String, Object> getTaskDetailsForArchive() {
        Map<String, Object> taskDetails = new HashMap();

        taskDetails.put("path", this.getPath());
        taskDetails.put("filename", this.getFilename());
        taskDetails.put("resourceId", this.getResourceId());
        taskDetails.put("locatorInfo", this.getLocatorInfo());
        taskDetails.put("deleteSource", this.getDeleteSource());
        taskDetails.put("details", this.getDetails());

        return taskDetails;
    }

    // Appends filename to the path
    public String getFullName() {
        String fullFileName = "";

        String path = this.getPath();
        if (!StringUtils.isBlank(path)) {
            if (!path.trim().endsWith("\\")) {
                path = path.trim().concat("\\");
            }
        }

        fullFileName = (path == null) ? "" : path + this.getFilename();
        return fullFileName;
    }

}
