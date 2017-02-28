package com.ykode.coral.dispatcher.exceptions;

import com.ykode.coral.core.Event;

public class EventHandlerNotFoundException extends RuntimeException {
  private Event<?> event;

  public EventHandlerNotFoundException(Event<?> event) {
    this.event = event;
  }

  public Event<?> getEvent() {
    return event;
  }

  @Override
  public String getMessage() {
    return "There is no handler for event: " + event.getClass().getCanonicalName();
  }
}
