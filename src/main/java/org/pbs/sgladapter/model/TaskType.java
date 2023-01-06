package org.pbs.sgladapter.model;

public enum TaskType {

    FILE_RESTORE("file_restore"),
    FILE_ARCHIVE("file_archive");

    private final String type;

    private TaskType(String type) {
        this.type = type;
    }

    //@JsonValue
    public String getType() {
        return type;
  }


}
