package com.ykode.coral.core;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * The interface containing the behaviour of a state when the event is applied or command executed.
 *
 * @param <S> The type of state managed by Aggregate.
 */
public interface Aggregate<S> {

  /**
   * This method creates a zero state. A state before creation.
   *
   * @return a 'zero' state, an invalid state before creation or \(S_0\)
   */
  S getZero();

  /**
   * Applies the event to a state according this function \(S_1 = apply(S_0, Event) \).
   *
   * @param state The state where the event to be applied.
   * @param event The event to be applied.
   * @return The result of the state where event is applied.
   */
  S apply(@Nonnull  S state, @Nonnull Event<S> event);

  /**
   * Executes command to a state, returns one or more events as the result of
   * command execution according function \([E_0..E_n] = exec(S, C)\).
   *
   * @param state The state where the command is executed against.
   * @param command The command to be executed.
   * @return The events emitted as the result of command execution.
   */
  List<Event<S>> exec(@Nonnull S state, @Nonnull Command<S> command) throws Exception;
}
