package com.bhinneka.coral.core.generators;

import com.bhinneka.coral.core.IdGenerator;

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
