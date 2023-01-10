package org.pbs.sgladapter.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String details) {
        super("Bad Request: [" + details + "]");
    }
}
