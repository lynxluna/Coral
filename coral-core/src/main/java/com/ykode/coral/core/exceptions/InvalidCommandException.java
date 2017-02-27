package com.ykode.coral.core.exceptions;

public class InvalidCommandException extends IllegalArgumentException {
  public InvalidCommandException(String message) {
    super(message);
    if (null == message) {
      message = "Command cannot be executed on state";
    }
  }
}
