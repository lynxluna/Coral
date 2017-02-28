package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.exceptions.InvalidCommandException;

/**
 * The validator interface.
 *
 * @param <S> type of the state.
 */
public interface Validator<S> {
  boolean validate(S state, Command<S> command) throws InvalidCommandException;
}
