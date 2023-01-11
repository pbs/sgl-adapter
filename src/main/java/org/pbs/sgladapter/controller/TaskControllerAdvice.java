package org.pbs.sgladapter.controller;


import org.pbs.sgladapter.exception.BadRequestException;
import org.pbs.sgladapter.exception.ErrorResponse;
import org.pbs.sgladapter.exception.JobNotFoundException;
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


  /**
   * Handles a BadRequestException, which is thrown if the vendor system was unable to create a
   * restore/archive job for a reason. It ensures the exception is treated as
   * a 400 - Bad Request.
   *
   * @param ex exception thrown when validation rules fail
   * @return response with failed validation and status 400
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public final ResponseEntity<Object> handleBadRequestExceptions(Exception ex, WebRequest request) {
    ErrorResponse exceptionResponse =
            new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }


  /**
   * Handles a ServiceUnavailableException, which is thrown if the vendor system was unable to return a
   * valid JSON response. It ensures the exception is treated as a 503 - Server Unavailable.
   *
   * @param ex exception thrown when validation rules fail
   * @return response with failed message and status 503
   */
  @ExceptionHandler(ServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public final ResponseEntity<Object> handleServiceUnavailableExceptions(Exception ex, WebRequest request) {
    ErrorResponse exceptionResponse =
            new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }

  /**
   * Handles a JobNotFoundException, which is thrown if the vendor system was unable to find the job
   * with passed in taskId. It ensures the exception is treated as a 404 - Not Found.
   *
   * @param ex exception thrown when validation rules fail
   * @return response with failed message and status 404
   */
  @ExceptionHandler(JobNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public final ResponseEntity<Object> handleJobNotFoundExceptions(Exception ex, WebRequest request) {
    ErrorResponse exceptionResponse =
            new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }
}
