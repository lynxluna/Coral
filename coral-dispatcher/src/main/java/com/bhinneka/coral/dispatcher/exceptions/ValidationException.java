package com.bhinneka.coral.dispatcher.exceptions;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;

public class ValidationException extends InvalidCommandException {
  private String additionalMessage;

  public ValidationException(Command<?> command, Object state) {
    super(command, state);
  }

  public ValidationException(Command<?> command, Object state, String additionalMessage) {
    super(command, state);
    this.additionalMessage = additionalMessage;
  }

  @Override
  public String getMessage() {
    if (additionalMessage == null || additionalMessage.isEmpty()) {
      return super.getMessage();
    }
    else {
      return super.getMessage() + "\nadditionalMessage: " + additionalMessage;
    }
  }
}
