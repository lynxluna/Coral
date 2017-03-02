package com.bhinneka.coral.core.handlers;

import javax.annotation.Nonnull;

/**
 * Asynchronous handler interface used to catch the error and the succsss.
 * Implement this method to continue and catch the value and error on asynchronous operation
 *
 * @param <T> the type of the result returned on successful call.
 */
public interface AsyncHandler<T> {

  /**
   * This method will be called on failure during asynchronous call.
   *
   * @param exception exception passed on failure.
   */
  void onError(final @Nonnull Exception exception);

  /**
   * This method will be called on successful asynchronous call.
   *
   * @param result the result passed on successful call.
   */
  void onSuccess(final @Nonnull T result);
}
