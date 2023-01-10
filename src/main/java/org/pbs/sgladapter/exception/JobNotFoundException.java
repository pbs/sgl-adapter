package org.pbs.sgladapter.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(String id) {
        super("No job found for Id " + id);
    }
}
