package ru.pihanya.compmath.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CollectionUtils {

  public static <T> List<T> copy(List<T> list) {
    Objects.requireNonNull(list);
    new ArrayList<>(list);

    final List<T> dest = newList(list.size());
    Collections.copy(dest, list);

    return dest;
  }

  public static <T> List<T> newList(int length) {
    return newList(length, null);
  }

  public static <T> List<T> newList(int length, T fill) {
    return new ArrayList<>(Collections.nCopies(length, fill));
  }
}
