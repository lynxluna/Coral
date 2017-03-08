package com.bhinneka.coral.dispatcher.exceptions;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;

public class ExecutorEmptyException extends InvalidCommandException {
  public ExecutorEmptyException(Command<?> command, Object state) {
    super(command, state);
  }
}
