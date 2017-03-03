package com.bhinneka.coral.core.codecs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public interface EntityIDCodec <M> {
  M encode(@Nonnull Object identity) throws IllegalArgumentException;

  @Nullable
  <T> T decode(@Nonnull M message);
}
