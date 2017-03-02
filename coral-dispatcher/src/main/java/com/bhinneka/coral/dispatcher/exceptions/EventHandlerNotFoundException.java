package com.bhinneka.coral.dispatcher.exceptions;

import com.bhinneka.coral.core.Event;

/**
 * Exception rasied when an {@link Event event} does dot have event handler.
 */
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
