package com.ykode.coral.core;

import java.io.IOException;

public interface Codec<T, U> {
  U encode(final T obj);
  T decode(U encodedObj) throws IOException;
}
