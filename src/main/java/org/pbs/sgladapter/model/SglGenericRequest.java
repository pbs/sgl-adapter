package org.pbs.sgladapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

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

    private String requestor;

    private String details;

    private Instant timestamp;

    private Map<String, String> error;

    public SglGenericRequest() {
        timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        error = new HashMap<>();
        error.put("description", "");
        error.put("detail", "");
    }

    public Map<String, Object> getTaskDetailsForRestore() {
        Map<String, Object> taskDetails = new HashMap();

        taskDetails.put("requestor", this.getRequestor());
        taskDetails.put("path", this.getPath());
        taskDetails.put("filename", this.getFilename());
        taskDetails.put("resourceId", this.getResourceId());
        taskDetails.put("details", this.getDetails());

        return taskDetails;
    }


    public Map<String, Object> getTaskDetailsForArchive() {
        Map<String, Object> taskDetails = new HashMap();

        taskDetails.put("requestor", this.getRequestor());
        taskDetails.put("path", this.getPath());
        taskDetails.put("filename", this.getFilename());
        taskDetails.put("resourceId", this.getResourceId());
        taskDetails.put("locatorInfo", this.getLocatorInfo());
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

//    public String getFormattedTimestamp() {
//        private static final String PATTERN_FORMAT = "MM/dd/yyyyT";
//    }

}
