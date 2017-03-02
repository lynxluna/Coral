package com.bhinneka.coral.stores;

import java.util.ArrayList;
import java.util.List;

class CollectionUtilities {
  public interface Predicate<T> {
    boolean apply(T type);
  }

  public static <T> List<T> filter(final List<T> target, final Predicate<T> predicate) {
    final List<T> result = new ArrayList<T>();
    for (T elem : target) {
      if (predicate.apply(elem)) {
        result.add(elem);
      }
    }

    return result;
  }
}
