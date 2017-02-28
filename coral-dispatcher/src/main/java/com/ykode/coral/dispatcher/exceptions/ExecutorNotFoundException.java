package com.ykode.coral.dispatcher.exceptions;

import com.ykode.coral.core.Command;

/**
 * Exception raised if there's no executor for a {@link Command command}.
 */
public class ExecutorNotFoundException extends RuntimeException {
  private Command<?> command;

  public ExecutorNotFoundException(Command<?> command) {
    this.command = command;
  }

  public Command<?> getCommand() {
    return command;
  }

  @Override
  public String getMessage() {
    return "There is no Executor for command: " + command.getClass().getCanonicalName();
  }
}
