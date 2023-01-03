package org.pbs.sgladapter.exception;

import java.util.Date;

public class ErrorResponse {
  private Date timestamp;
  private String message;
  private String details;

  public ErrorResponse(Date date, String message, String description) {
    this.timestamp = date;
    this.message = message;
    this.details = description;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
