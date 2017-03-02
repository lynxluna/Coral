package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;

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

  List<Event<S>> execute(S state, Command<S> command)
      throws ExecutorEmptyException, ExecutorNotFoundException {
    if (executorMap.isEmpty()) {
      throw new ExecutorEmptyException();
    }
    final ExecutorPair<S> pair = executorMap.get(command.getClass());
    if (pair == null) {
      throw new ExecutorNotFoundException(command);
    }
    return pair.execute(state, command);
  }
}
