package ru.pihanya.compmath.pivot;

import java.util.List;
import ru.pihanya.compmath.AlgorithmResult;
import ru.pihanya.compmath.math.Matrix;

public interface PivotAlgorithmResult extends AlgorithmResult {

  Matrix<Double> getTriangleMatrix();

  double getDeterminant();

  List<Double> getSolution();

  List<Double> getDiscrepancies();
}
