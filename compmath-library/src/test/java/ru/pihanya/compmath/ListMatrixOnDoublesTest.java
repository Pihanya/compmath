package ru.pihanya.compmath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;

public class ListMatrixOnDoublesTest {
  public static ListMatrix<Double> matrix;

  @BeforeEach
  public void init() {
    matrix = new ListMatrix<>();
  }

  @Test
  public void addColumnTest() {
    List<Double> doubles = Arrays.asList(1D, 2D, 3D, 4D);

    ImmutableMatrix<Double> newMatrix = matrix
        .addColumn(1, doubles);

    assertEquals(doubles, newMatrix.getColumn(1));
    assertEquals(doubles.size(), newMatrix.getHeight());
    assertEquals(1, newMatrix.getWidth());
  }

  @Test
  public void addRowTest() {
    List<Double> doubles = Arrays.asList(1D, 2D, 3D, 4D);

    ImmutableMatrix<Double> newMatrix = matrix
        .addRow(1, doubles);

    assertEquals(doubles, newMatrix.getRow(1));
    assertEquals(doubles.size(), newMatrix.getWidth());
    assertEquals(1, newMatrix.getHeight());
  }
}
