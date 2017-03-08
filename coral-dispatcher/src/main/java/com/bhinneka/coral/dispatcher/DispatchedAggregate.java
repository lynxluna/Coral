package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Aggregate;
import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;

import javax.annotation.Nonnull;
import java.util.List;

public class DispatchedAggregate<S> implements Aggregate<S> {
  private S zero;
  private ExecutorManager<S> executorManager;
  private ValidationManager<S> validationManager;
  private EventHandlerManager<S> eventHandlerManager;

  private DispatchedAggregate(@Nonnull S zero,
                              @Nonnull ExecutorManager<S> executorManager,
                              @Nonnull ValidationManager<S> validationManager,
                              @Nonnull EventHandlerManager<S> eventHandlerManager) {
    this.zero = zero;
    this.executorManager = executorManager;
    this.validationManager = validationManager;
    this.eventHandlerManager = eventHandlerManager;
  }

  @Override
  public S getZero() {
    return zero;
  }

  @Override
  public List<Event<S>> exec(@Nonnull S state, @Nonnull Command<S> command) throws InvalidCommandException {
    validationManager.validate(state, command); // this will raise exception on error
    return executorManager.execute(state, command);
  }

  @Override
  public S apply(@Nonnull S state, @Nonnull Event<S> event) throws Exception {
    return eventHandlerManager.apply(state, event);
  }

  @Nonnull public static <S> Builder<S> newBuilder(S zero) {
    return new Builder<S>(zero);
  }

  public static class Builder<S> {
    private S zero;
    private ExecutorManager.Builder<S> executorManagerBuilder;
    private ValidationManager.Builder<S> validationManagerBuilder;
    private EventHandlerManager.Builder<S> eventHandlerManagerBuilder;

    private Builder(S zero) {
      this.zero = zero;
      this.executorManagerBuilder = ExecutorManager.<S>newBuilder();
      this.validationManagerBuilder = ValidationManager.<S>newBuilder();
      this.eventHandlerManagerBuilder = EventHandlerManager.<S>newBuilder();
    }

    @Nonnull public final DispatchedAggregate<S> build() {
      return new DispatchedAggregate<S>(this.zero,
          executorManagerBuilder.build(),
          validationManagerBuilder.build(),
          eventHandlerManagerBuilder.build());
    }

    final Builder<S> addExecutor(Class<? extends Command<S>> klass, Executor<S> executor) {
      executorManagerBuilder.addExecutor(klass, executor);
      return this;
    }

    final Builder<S> addValidator(Class<? extends Command<S>> klass, Validator<S> validator) {
      validationManagerBuilder.addValidator(klass, validator);
      return this;
    }

    final Builder<S> addEventHandler(Class<? extends Event<S>> klass, EventHandler<S> eventHandler) {
      eventHandlerManagerBuilder.addEventHandler(klass, eventHandler);
      return this;
    }
  }

}
