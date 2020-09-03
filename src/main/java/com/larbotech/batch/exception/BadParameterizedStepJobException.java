package com.larbotech.batch.exception;

public class BadParameterizedStepJobException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public BadParameterizedStepJobException() {
    }

    public BadParameterizedStepJobException(String message) {
        super(message);
    }

    public BadParameterizedStepJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadParameterizedStepJobException(Throwable cause) {
        super(cause);
    }
}
