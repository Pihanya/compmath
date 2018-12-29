package ru.pihanya.compmath.equations;

import java.util.ArrayList;
import java.util.List;
import ru.pihanya.compmath.AlgorithmExecutor;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;
import ru.pihanya.compmath.math.Matrix;

public class GaussianPivotAlgorithmExecutor implements
    AlgorithmExecutor<Matrix<Double>> {

  @Override
  public GaussianAlgorithmResult execute(Matrix<Double> sourceExtendedMatrix) {
    ImmutableMatrix<Double> matrix = new ListMatrix<>(sourceExtendedMatrix);

    for (int k = 1; k < matrix.getHeight(); ++k) {
      int pivotY = findLargestPivot(matrix, k);
      if (pivotY > k) {
        matrix = matrix.swapRows(k, pivotY);
        pivotY = k;
      }

      if (pivotY == -1) {
        return new GaussianAlgorithmResult() {
          @Override
          public int getErrorCode() {
            return 1;
          }
        };
      }

      for (int y = k + 1; y <= matrix.getHeight(); ++y) {
        double m = -(matrix.get(y, k) / matrix.get(pivotY, k));
        for (int x = 1; x <= matrix.getWidth(); ++x) {
          double elem = matrix.get(y, x);
          double elemFromPivotRow = matrix.get(pivotY, x);
          elem = elem + m * elemFromPivotRow;
          matrix = matrix.set(y, x, elem);

        }
      }
    }

    ImmutableMatrix<Double> finalMatrix = matrix;

    double determinant = countDeterminant(matrix);

    List<Double> xs = new ArrayList<>(matrix.getWidth() - 1);
    for (int i = 0; i < matrix.getHeight(); ++i) {
      xs.add(0D);
    }

    for (int y = matrix.getHeight(); y >= 1; --y) {
      double ans = matrix.get(y, matrix.getWidth());
      int x = matrix.getHeight();
      for (; x > y; --x) {
        ans -= (xs.get(x - 1) * matrix.get(y, x));
      }
      xs.set(x - 1, ans / matrix.get(y, x));
    }

    List<Double> discrepancies = countDiscrepancies(sourceExtendedMatrix, xs);

    return new GaussianAlgorithmResult() {
      @Override
      public Matrix<Double> getTriangleMatrix() {
        return finalMatrix;
      }

      @Override
      public double getDeterminant() {
        return determinant;
      }

      @Override
      public List<Double> getSolution() {
        return xs;
      }

      @Override
      public List<Double> getDiscrepancies() {
        return discrepancies;
      }
    };
  }

  private static List<Double> countDiscrepancies(Matrix<Double> matrix,
      List<Double> answers) {
    List<Double> discrepancies = new ArrayList<>(matrix.getHeight());
    for (int y = 1; y <= matrix.getHeight(); ++y) {
      double localDiscrepancy = matrix.get(y, matrix.getWidth());
      for (int x = 1; x <= matrix.getWidth() - 1; ++x) {
        localDiscrepancy -= answers.get(x - 1) * matrix.get(y, x);
      }
      discrepancies.add(Math.abs(localDiscrepancy));
    }
    return discrepancies;
  }

  private static int findLargestPivot(Matrix<Double> extendedMatrix, int col) {
    assert col >= 1 && col < extendedMatrix.getWidth();

    Double pivot = 0D;
    int index = -1;
    for (int y = (col <= extendedMatrix.getHeight() ? col : 1); y <= extendedMatrix.getHeight();
        ++y) {
      if (Math.abs(extendedMatrix.get(y, col)) > pivot) {
        index = y;
        pivot = extendedMatrix.get(y, col);
      }
    }

    return index;
  }

  private static double countDeterminant(Matrix<Double> triangleMatrix) {
    double determinant = Double.NaN;
    for (int i = 1; i <= triangleMatrix.getHeight(); i++) {
      if (Double.isNaN(determinant)) {
        determinant = triangleMatrix.get(i, i);
      } else {
        determinant *= triangleMatrix.get(i, i);
      }
    }

    return determinant;
  }
}