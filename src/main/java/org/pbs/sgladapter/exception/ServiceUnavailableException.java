package org.pbs.sgladapter.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {
        super("Unable to get a response from the server");
    }

}
