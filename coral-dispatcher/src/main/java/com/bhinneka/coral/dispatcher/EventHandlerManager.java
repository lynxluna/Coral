package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.dispatcher.exceptions.EventHandlerEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.EventHandlerNotFoundException;

import java.util.HashMap;

class EventHandlerManager<S> {
  private HashMap<Class<? extends Event<S>>, EventHandler<S>> handlerMap;

  EventHandlerManager() {
    handlerMap = new HashMap<Class<? extends Event<S>>, EventHandler<S>>();
  }

  S handle(S state, Event<S> event) throws
      EventHandlerNotFoundException, EventHandlerEmptyException {
    
    if (handlerMap.isEmpty()) {
      throw new EventHandlerEmptyException();
    }

    final EventHandler<S> handler = handlerMap.get(event.getClass());

    if (handler == null) {
      throw new EventHandlerNotFoundException(event);
    }
    return handler.apply(state, event);
  }

  void addEventHandler(Class<? extends Event<S>> eventType, EventHandler<S> handler) {
    handlerMap.put(eventType, handler);
  }
}
