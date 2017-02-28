package com.ykode.coral.dispatcher.exceptions;

/**
 * An exception raised when there's no event handler.
 */
public class EventHandlerEmptyException extends RuntimeException {

  @Override
  public String getMessage() {
    return "No Handler can be found. Please add one if you want to process event.";
  }
}
