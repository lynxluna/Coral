package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.Event;

import java.util.HashMap;
import java.util.List;

class ExecutorManager<S> {
  private HashMap<Class<? extends Command<S>>, ExecutorPair<S>> executorMap;

  ExecutorManager() {
    executorMap = new HashMap<Class<? extends Command<S>>, ExecutorPair<S>>(5);
  }

  void addExecutor(Class<? extends Command<S>> type, Executor<S> executor) {
    executorMap.put(type, new ExecutorPair<S>(executor));
  }

  void addExecutor(Class<? extends Command<S>> type, Executor<S> executor,
                          Validator<S> validator) {
    executorMap.put(type, new ExecutorPair<S>(executor, validator));
  }

  List<Event<S>> execute(S state, Command<S> command) {
    final ExecutorPair<S> pair = executorMap.get(command.getClass());
    return pair.execute(state, command);
  }
}
