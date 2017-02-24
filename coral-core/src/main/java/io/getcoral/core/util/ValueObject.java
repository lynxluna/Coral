package io.getcoral.core.util;

/**
 * The interface for a ValueObject. A Value object must have copy builder
 *
 * @param <TBuilder> The builder of value object
 */
public interface ValueObject<TBuilder> {
  TBuilder CopyBuilder();
}
