package com.bhinneka.coral.dispatcher.exceptions;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;

public class ExecutorNotFoundException extends InvalidCommandException {
  public ExecutorNotFoundException(Command<?> command, Object state) {
    super(command, state);
  }
}
