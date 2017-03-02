package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Event;

/**
 * The event handler interface
 *
 * @param <S> the type of state.
 */
public interface EventHandler<S> {
  S apply(S state, Event<S> event);
}
