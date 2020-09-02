package com.larbotech.batch.exception;

public class BatchNameNotFoundException extends RuntimeException {

  public BatchNameNotFoundException() {
  }

  public BatchNameNotFoundException(String message) {
    super(message);
  }
}
