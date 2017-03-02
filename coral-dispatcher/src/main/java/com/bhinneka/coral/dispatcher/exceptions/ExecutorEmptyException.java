package com.bhinneka.coral.dispatcher.exceptions;

/**
 * Exception raised when executor is empty.
 */
public class ExecutorEmptyException extends RuntimeException {
  @Override
  public String getMessage() {
    return "There's no executor defined for this Aggregate";
  }
}
