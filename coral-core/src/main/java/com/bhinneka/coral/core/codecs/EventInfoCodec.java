package com.bhinneka.coral.core.codecs;

import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.EventInfo;

public interface EventInfoCodec<M> {
  M encode(EventInfo<?, ?> info);
  <I, S> EventInfo<I, S> decode(M msg, Class<? extends Event<S>> eventKlass)
      throws Exception;
}
