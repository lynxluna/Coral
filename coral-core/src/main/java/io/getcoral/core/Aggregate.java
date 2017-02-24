package io.getcoral.core;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The interface containing the behaviour of a state when the event is applied or command executed
 *
 * @param <S> The State where aggregate is managing
 */
public interface Aggregate<S> {

  /**
   * This method creates a zero state. A state before creation
   *
   * @return a 'zero' state, usually invalid state before creation
   */
  public S getZero();

  /**
   * Applies the event to a state, returns new state on which the event is applied
   *
   * @param state The state where the event to be applied
   * @param event The event to be applied
   * @return The result of the state where event is applied
   */
  public S apply(@Nonnull  S state, @Nonnull Event<S> event);

  /**
   * Executes command to a state, returns one or more events as the result of command execution.
   *
   * @param state The state where the command is executed against
   * @param command The command to be executed
   * @return The events emitted as the result of command execution
   */
  public List<Event<S>> exec(@Nonnull S state, @Nonnull Command<S> command);
}
