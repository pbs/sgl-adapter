package org.pbs.sgladapter.model;
public enum TaskStatus {

    CREATED("Created"),
    SUBMITTED("Submitted"),
    IN_PROGRESS("In Progress"),
    COMPLETED_SUCCESS("Succeeded"),
    COMPLETED_FAILED("Failed");

    private final String status;

    private TaskStatus(String status) {
        this.status = status;
    }

    //@JsonValue
    public String getStatus() {
        return status;
    }


}
