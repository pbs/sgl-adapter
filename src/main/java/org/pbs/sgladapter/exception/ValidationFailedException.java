package org.pbs.sgladapter.exception;

public class ValidationFailedException extends RuntimeException {

  public ValidationFailedException(String fieldName) {
    super("Invalid value for: " + fieldName);
  }
}
