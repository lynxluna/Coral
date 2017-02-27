package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Event;

public interface EventHandler<S> {
  S apply(S state, Event<S> event);
}
