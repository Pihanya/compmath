package ru.pihanya.compmath.integration;

import ru.pihanya.compmath.AlgorithmResult;

public interface IntegrationAlgorithmResult extends AlgorithmResult {
  default double getIntegrationResult() {
    return Double.NaN;
  }

  default double getErrorValue() {
    return Double.NaN;
  }

  default long getIterationsAmount() {
    return -1;
  }
}
