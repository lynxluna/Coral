package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.Event;

import java.util.List;

public interface Executor<S> {
  List<Event<S>> execute(S state, Command<S> command);
}
