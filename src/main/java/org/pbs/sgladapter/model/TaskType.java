package org.pbs.sgladapter.model;

public enum TaskType {

  FILE_RESTORE("FileRestore"),
  FILE_ARCHIVE("FileArchive");

  private final String type;

  private TaskType(String type) {
    this.type = type;
  }

  //@JsonValue
  public String getType() {
    return type;
  }


}
