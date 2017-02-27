package com.ykode.coral.dispatcher;

import com.ykode.coral.core.Command;
import com.ykode.coral.core.exceptions.InvalidCommandException;

public interface Validator<S> {
  void validate(S state, Command<S> command) throws InvalidCommandException;
}
