package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Aggregate;
import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * An aggregate with dispatcher behaviour.
 *
 * @param <S> the state to be managed by the aggregate.
 */
public final class DispatchAggregate<S> implements Aggregate<S> {
  private ExecutorManager<S> executors;
  private EventHandlerManager<S> eventHandlers;
  private S zero;

  /**
   * Construct a Dispatch Aggregate with zero state
   *
   * @param zero the 'zero' state before creation.
   */
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
                 @Nonnull Event<S> event) throws Exception {
    return eventHandlers.handle(state, event);
  }

  @Override
  public S getZero() {
    return zero;
  }

  /**
   * Adding handler for some command type assuming passthrough
   * validator.
   *
   * @param commandType the command type.
   * @param executor The executor instance for this command class.
   */
  public void addHandler(@Nonnull Class<? extends Command<S>> commandType,
                  @Nonnull Executor<S> executor) {
    executors.addExecutor(commandType, executor);
  }

  /**
   * Adding handler for a command type with validator.
   *
   * @param commandType command type.
   * @param executor executor for a command type.
   * @param validator the validator to validate if the executed command is valid.
   */
  public void addHandler(@Nonnull Class<? extends Command<S>> commandType,
                  @Nonnull Executor<S> executor,
                  @Nonnull Validator<S> validator) {
    executors.addExecutor(commandType, executor, validator);
  }

  /**
   * Adding handle to apply event to state.
   *
   * @param eventType the event type.
   * @param eventHandler the event handler for the event.
   */
  public void addHandler(@Nonnull Class<? extends Event<S>> eventType,
                  @Nonnull EventHandler<S> eventHandler) {
    eventHandlers.addEventHandler(eventType, eventHandler);
  }
}
