package ru.pihanya.compmath;

public interface AlgorithmResult {

  default boolean isSuccessful() {
    return getErrorCode() <= 0;
  }

  default int getErrorCode() {
    return -1;
  }
}
