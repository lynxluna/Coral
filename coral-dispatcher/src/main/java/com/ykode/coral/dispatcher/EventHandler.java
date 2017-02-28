package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Event;

/**
 * The event handler interface
 *
 * @param <S> the type of state.
 */
public interface EventHandler<S> {
  S apply(S state, Event<S> event);
}
