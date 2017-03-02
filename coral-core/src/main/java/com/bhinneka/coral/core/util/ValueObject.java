package com.bhinneka.coral.core.util;

/**
 * The interface for a ValueObject. A Value object must have copy builder
 *
 * @param <T> The builder of value object
 */
public interface ValueObject<T> {
  T copyBuilder();
}
