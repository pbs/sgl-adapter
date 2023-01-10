package org.pbs.sgladapter.controller;


import org.pbs.sgladapter.exception.BadRequestException;
import org.pbs.sgladapter.exception.ErrorResponse;
import org.pbs.sgladapter.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice()
public class TaskControllerAdvice {

  /**
   * Handles a MethodArgumentNotValidException, which is thrown if the Task passed into
   * the POST /task endpoint fails any validation rules. It ensures the exception is treated as
   * a 400 - Bad Request and that the response includes any specific validation error messages
   * associated with the invalid field values.
   *
   * @param ex exception thrown when validation rules fail
   * @return map of fields and associated error messages
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public final ResponseEntity<Object> badRequestHandler(Exception ex, WebRequest request) {
    ErrorResponse exceptionResponse =
            new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(ServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public final ResponseEntity<Object> serviceUnavailableHandler(Exception ex, WebRequest request) {
    ErrorResponse exceptionResponse =
            new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
