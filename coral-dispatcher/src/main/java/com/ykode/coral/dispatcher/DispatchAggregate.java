package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Aggregate;
import com.ykode.coral.core.Command;
import com.ykode.coral.core.Event;
import com.ykode.coral.core.exceptions.InvalidCommandException;
import com.ykode.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.ykode.coral.dispatcher.exceptions.ExecutorNotFoundException;

import java.util.List;
import javax.annotation.Nonnull;

public class DispatchAggregate<S> implements Aggregate<S> {
  private ExecutorManager<S> executors;
  private EventHandlerManager<S> eventHandlers;
  private S zero;

  public DispatchAggregate(@Nonnull S zero) {
    this.zero = zero;
    this.executors = new ExecutorManager<S>();
    this.eventHandlers = new EventHandlerManager<S>();
  }

  @Override
  public List<Event<S>> exec(@Nonnull S state,
                             @Nonnull Command<S> command) throws
      InvalidCommandException,
      ExecutorNotFoundException,
      ExecutorEmptyException {
    return executors.execute(state, command);
  }

  @Override
  public S apply(@Nonnull S state,
                 @Nonnull Event<S> event) {
    return eventHandlers.handle(state, event);
  }

  @Override
  public S getZero() {
    return zero;
  }

  public void addHandler(@Nonnull Class<? extends Command<S>> commandType,
                  @Nonnull Executor<S> executor) {
    executors.addExecutor(commandType, executor);
  }

  public void addHandler(@Nonnull Class<? extends Command<S>> commandType,
                  @Nonnull Executor<S> executor,
                  @Nonnull Validator<S> validator) {
    executors.addExecutor(commandType, executor, validator);
  }

  public void addHandler(@Nonnull Class<? extends Event<S>> eventType,
                  @Nonnull EventHandler<S> eventHandler) {
    eventHandlers.addEventHandler(eventType, eventHandler);
  }
}
