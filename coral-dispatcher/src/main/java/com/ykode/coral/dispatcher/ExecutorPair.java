package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.Event;
import com.ykode.coral.core.exceptions.InvalidCommandException;

import java.util.List;

class ExecutorPair<S> {
  private Executor<S> executor;
  private Validator<S> validator;

  private Validator<S> passThrough = new Validator<S>() {
    @Override
    public void validate(S state, Command<S> command) throws InvalidCommandException {
      // do nothing
    }
  };

  ExecutorPair(Executor<S> executor, Validator<S> validator) {
    this.executor = executor;
    this.validator = validator;
  }

  ExecutorPair(Executor<S> executor) {
    this.executor = executor;
    this.validator = passThrough;
  }

  List<Event<S>> execute(S state, Command<S> command) {
    this.validator.validate(state, command);
    return this.execute(state, command);
  }
}
