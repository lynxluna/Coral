package com.bhinneka.coral.core.exceptions;

import com.bhinneka.coral.core.Command;

/**
 * An Exception raised on illegal command execution.
 */
public class InvalidCommandException extends IllegalArgumentException {

  private String message;
  private Command<?> command;
  private Object state;

  /**
   * Construct an exception during excetion of a command to a state.
   *
   * @param command the executing command
   * @param state the state where the command is executed
   */
  public InvalidCommandException(final Command<?> command, final Object state) {
    this.command = command;
    this.state   = state;
    message = "Invalid command to be executed on state.\n"
        + "Command: " + command.toString() + "\n"
        + "State:" + state.toString();
  }

  /**
   * Gets the command causing exception.
   *
   * @return Command causing exception
   */
  public Command<?> getCommand() {
    return command;
  }

  /**
   * Gets the state which command is executed against.
   *
   * @return the target state
   */
  public Object getState() {
    return state;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
