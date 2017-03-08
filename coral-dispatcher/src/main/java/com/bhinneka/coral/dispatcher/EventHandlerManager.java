package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Event;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.HashMap;

public class EventHandlerManager<S> {
  private HashMap<Type, EventHandler<S>> eventHandlers;

  private EventHandlerManager(HashMap<Type, EventHandler<S>> eventHandlers) {
    this.eventHandlers = eventHandlers;
  }

  final S apply(S state, Event<S> event) {
    if (eventHandlers.isEmpty()) {
      return state;
    }

    final EventHandler<S> eventHandler = eventHandlers.get(event.getClass());

    if (eventHandler == null) {
      return state;
    }

    return eventHandler.apply(state, event);
  }

  @Nonnull public final Builder<S> newBuilder() {
    return new Builder<S>();
  }

  public static class Builder<S> {
    private HashMap<Type, EventHandler<S>> eventHandlers;

    private Builder() {
      eventHandlers = new HashMap<Type, EventHandler<S>>();
    }

    @Nonnull final EventHandlerManager<S> build() {
      return new EventHandlerManager<S>(eventHandlers);
    }

    final Builder<S> addEventHandler(@Nonnull Class<? extends Event<S>> klass,
                                     @Nonnull final EventHandler<S> eventHandler) {
      eventHandlers.put(klass, eventHandler);
      return this;
    }
  }
}
