package com.ykode.coral.core.util;

/**
 * Implement this to mark the builder of Value Object
 *
 * @param <T> The type of value object to be built.
 */
public interface Builder<T> {
  T build();
}
