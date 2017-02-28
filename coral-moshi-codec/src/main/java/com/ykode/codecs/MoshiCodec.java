package com.ykode.codecs;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.ykode.coral.core.Codec;

import java.io.IOException;

public class MoshiCodec<T extends Object> implements Codec<T, String> {

  private Moshi moshi;
  private JsonAdapter<T> adapter;

  public MoshiCodec(Class<T> tClass) {
    moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(tClass);
  }

  @Override
  public String encode(T obj) {
    return adapter.toJson(obj);
  }

  @Override
  public T decode(String encodedObj) throws IOException {
    return adapter.fromJson(encodedObj);
  }
}
