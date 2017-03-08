package com.bhinneka.coral.dispatcher;

import com.bhinneka.coral.core.Command;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ValidationManager<S> {
  private HashMap<Type, List<Validator<S>>> validatorMap;

  private ValidationManager(HashMap<Type, List<Validator<S>>> validatorMap) {
    this.validatorMap = validatorMap;
  }

  void validate(@Nonnull S state,
                @Nonnull Command<S> command) {
    if (validatorMap.isEmpty()) {
      return; // passthrough
    }

    final List<Validator<S>> validators = validatorMap.get(command.getClass());

    if (validators == null || validators.isEmpty()) {
      return; // passthrough
    }

    for (Validator<S> v : validators) {
      v.validate(state, command);
    }
  }

  public Builder<S> newBuilder() {
    return new Builder<S>();
  }

  public static class Builder<S> {
    private HashMap<Type, List<Validator<S>>> validatorMap;

    public Builder() {
      validatorMap = new HashMap<Type, List<Validator<S>>>();
    }

    @Nonnull final ValidationManager<S> build() {
      return new ValidationManager<S>(validatorMap);
    }

    void addValidator(@Nonnull Class<? extends Command<S>> klass,
                      @Nonnull Validator<S> validator) {
      final List<Validator<S>> validators = validatorMap.get(klass);
      List<Validator<S>> newValidators;

      if (validators == null) {
        newValidators = Collections.singletonList(validator);
        validatorMap.put(klass, newValidators);
      }
      else {
        newValidators = validators;
        if (!validators.contains(validator)) {
          newValidators.add(validator);
        }
        validatorMap.put(klass, newValidators);
      }
    }
  }
}
