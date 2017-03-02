package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.Event;

import java.util.List;

/**
 * Executor intorface.
 *
 * @param <S> the state type.
 */
public interface Executor<S> {
  List<Event<S>> execute(S state, Command<S> command);
}
