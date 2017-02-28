package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.Event;
import com.ykode.coral.core.exceptions.InvalidCommandException;

import java.util.Collections;
import java.util.List;

class ExecutorPair<S> {
  private Executor<S> executor;
  private Validator<S> validator;

  ExecutorPair(Executor<S> executor, Validator<S> validator) {
    this.executor = executor;
    this.validator = validator;
  }

  ExecutorPair(Executor<S> executor) {
    final Validator<S> passThrough = new Validator<S>() {
      @Override
      public boolean validate(S state, Command<S> command) throws InvalidCommandException {
        return true;
      }
    };

    this.executor = executor;
    this.validator = passThrough;
  }

  List<Event<S>> execute(S state, Command<S> command) throws InvalidCommandException {
    if (this.validator.validate(state, command)) {
      return this.executor.execute(state, command);
    }
    else {
      return Collections.emptyList();
    }
  }
}