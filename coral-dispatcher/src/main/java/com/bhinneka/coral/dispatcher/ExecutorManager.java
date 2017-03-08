package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.HashMap;

public class ExecutorManager<S> {
  private HashMap<Type, Executor<S>> executors;
  private ExecutorManager(HashMap<Type, Executor<S>> executors) {
    this.executors = executors;
  }

  public void execute(@Nonnull S state,
                      @Nonnull Command<S> command) throws InvalidCommandException {

    if (executors.isEmpty()) {
      throw new ExecutorEmptyException(command, state);
    }

    final Executor<S> executor = executors.get(command.getClass());
    if (executor == null) {
      throw new ExecutorNotFoundException(command, state);
    }
    else {
      executor.execute(state, command);
    }
  }

  @Nonnull public static <S> Builder<S> newBuilder() {
    return new Builder<S>();
  }

  public static class Builder<S> {
    private HashMap<Type, Executor<S>> executors;

    private Builder() {
      executors = new HashMap<Type, Executor<S>>();
    }

    @Nonnull final ExecutorManager<S> build() {
      return new ExecutorManager<S>(executors);
    }

    final Builder<S> addExecutor(Class<? extends Command<S>> klass, Executor<S> executor) {
      executors.put(klass, executor);
      return this;
    }
  }
}
