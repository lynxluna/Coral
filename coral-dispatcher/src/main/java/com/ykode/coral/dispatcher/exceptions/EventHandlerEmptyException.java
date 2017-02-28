package com.ykode.coral.dispatcher.exceptions;

import com.ykode.coral.core.Event;

public class EventHandlerEmptyException extends Exception {

  @Override
  public String getMessage() {
    return "No Handler can be found. Please add one if you want to process event.";
  }
}
