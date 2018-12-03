package ru.pihanya.compmath.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;
import ru.pihanya.compmath.math.Matrix;

public class MatrixUtils {

  public static double getDeterminant(ImmutableMatrix<Double> matrix) {
    final int y = 0;

    double determinant = 0.0D;
    for (int x = 0; x < matrix.getWidth(); x++) {
      final double sign = ((x + y) % 2 == 0) ? 1.0D : -1.0D;

      final Double value = matrix.get(y, x);
      final double minor = getMinor(matrix, y, x);

      determinant += sign * value * minor;
    }

    return determinant;
  }

  public static double getMinor(ImmutableMatrix<Double> matrix, int y, int x) {
    return (matrix.getHeight() == 0) || (matrix.getWidth() == 0) ? 0
        : getDeterminant(matrix.removeRow(y).removeColumn(x));
  }


  public static Matrix<Double> getRandomMatrix(int height, int width, int bound) {
    List<List<Double>> elements = new ArrayList<>(height);
    for (int y = 0; y < height; ++y) {
      elements.add(
          Stream.generate(() -> ((int) Math.ceil(Math.random() - 0.5D)) * (Math.random() * bound))
              .limit(width)
              .collect(Collectors.toList()));
    }
    return new ListMatrix<>(height, width, elements);
  }
}
