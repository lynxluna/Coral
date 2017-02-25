package com.ykode.coral.core.generators;

import com.ykode.coral.core.IdGenerator;

import java.util.UUID;

/**
 * Built-in generator for {@link UUID UUID} type.
 */
public class UuidGenerator implements IdGenerator<UUID> {
  @Override
  public UUID nextValue() {
    return UUID.randomUUID();
  }
}
