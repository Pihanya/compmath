package ru.pihanya.compmath.equations;

import java.util.List;
import ru.pihanya.compmath.AlgorithmResult;
import ru.pihanya.compmath.math.Matrix;

public interface GaussianAlgorithmResult extends AlgorithmResult {

  default Matrix<Double> getTriangleMatrix() {
    return null;
  }

  default double getDeterminant() {
    return Double.NaN;
  }

  default List<Double> getSolution() {
    return null;
  }

  default List<Double> getDiscrepancies() {
    return null;
  }
}
