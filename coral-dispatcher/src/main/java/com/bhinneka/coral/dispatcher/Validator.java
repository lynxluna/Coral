package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;

/**
 * The validator interface.
 *
 * @param <S> type of the state.
 */
public interface Validator<S> {
  boolean validate(S state, Command<S> command) throws InvalidCommandException;
}
