package com.ykode.coral.dispatcher.exceptions;

public class ExecutorEmptyException extends RuntimeException{
  @Override
  public String getMessage() {
    return "There's no executor defined for this Aggregate";
  }
}
