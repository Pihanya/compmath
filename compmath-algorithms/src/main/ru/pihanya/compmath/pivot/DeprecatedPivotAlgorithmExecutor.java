package ru.pihanya.compmath.pivot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javafx.util.Builder;
import ru.pihanya.compmath.AlgorithmExecutor;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;
import ru.pihanya.compmath.math.Matrix;
import ru.pihanya.compmath.utils.ConstantValues;

public class DeprecatedPivotAlgorithmExecutor implements AlgorithmExecutor {

  // Constant data block
  private final int dim;
  private final Matrix<Double> originalMatrix;

  public DeprecatedPivotAlgorithmExecutor(Matrix<Double> matrix) {
    Objects.requireNonNull(matrix);

    this.originalMatrix = matrix;
    this.dim = matrix.getHeight();
  }

  @Override
  public PivotAlgorithmResult execute() {
    ImmutableMatrix<Double> matrix = new ListMatrix<>(originalMatrix);

    // Let's find out the row echelon form of given matrix
    for (int k = 0; k < dim; ++k) {
      // Searching for pivot element in k-th column in rows in interval k+1...N
      // In other words, we are trying to find out the biggest element below element A[k, k]
      int pivotRow = k;
      Double pivotValue = matrix.get(pivotRow, k);
      for (int i = k + 1; i < dim; ++i) {
        if (Math.abs(matrix.get(i, k)) > pivotValue) {
          pivotValue = matrix.get(i, k);
          pivotRow = i;
        }
      }

      // If the principal diagonal element is zero, then matrix is singular
      if (Math.abs(matrix.get(k, pivotRow)) < ConstantValues.DOUBLE_MACHINE_EPSILON) {
        // Then return error code 1
        return new PivotAlgorithmResultBuilder()
            .withSuccess(false)
            .withErrorCode(1)
            .build();
      }

      // Swap row with pivot element and k-th row
//      for (int j = 0; j <= dim; ++j) { todo
      for (int j = 0; j < dim; ++j) {
        matrix = matrix
            .set(pivotRow, j, matrix.get(k, j))
            .set(k, j, matrix.get(pivotRow, j));
      }

      // Now we can use the formula: A[i, j] = A[i, j] - A[k, j] * F(i, k)
      // Where F(i, k) = A[i, k] / A[k, k]; i in [1; N]; j in [1; N + 1]
      for (int i = k + 1; i < dim; ++i) {
        Double F = matrix.get(i, k) / matrix.get(k, k);

//        for (int j = k + 1; j <= dim; ++j) { todo
        for (int j = k + 1; j < dim; ++j) {
          matrix = matrix.set(i, j,
              matrix.get(i, j) - matrix.get(k, j) * F);
        }

        matrix = matrix.set(i, k, 0.0D);
      }

      System.out.println("\r\n\r\n" + matrix);
    }

    // Backward substitution
    List<Double> resultVector = new ArrayList<>(Collections.nCopies(dim, null));
    for (int i = dim - 1; i >= 0; --i) {
//      resultVector.set(i, matrix.get(i, dim)); todo
      resultVector.set(i, matrix.get(i, dim - 1));

      for (int j = i + 1; j < dim; ++j) {
        resultVector
            .set(i, resultVector.get(i) - matrix.get(i, j) * resultVector.get(j));
      }

      resultVector.set(i, resultVector.get(i) / matrix.get(i, i));
    }

    // Algorithm has ended its work. Let's gather results together
    double determinant = 1;
    for (int i = 0; i < dim; ++i) {
      determinant *= matrix.get(i, i);
    }

    // Searching for discrepancies
    Vector<Double> discrepancies = new Vector<>(dim - 1);
    for (int i = 0; i < dim; ++i) {
//      double F = originalMatrix.get(i, dim); todo
      double F = originalMatrix.get(i, dim - 1);

      for (int j = 0; j < dim; ++j) {
        F -= (originalMatrix.get(i, j) * resultVector.get(j));
      }

      discrepancies.add(F);
    }

    return new PivotAlgorithmResultBuilder()
        .withTriangleMatrix(matrix) // Matrix is still in triangular form
        .withDeterminant(determinant)
        .withSolution(resultVector)
        .withDiscrepancies(discrepancies)
        .build();
  }

  private static class PivotAlgorithmResultBuilder implements Builder<PivotAlgorithmResult> {

    private boolean success = true;
    private int executionCode = 0;

    private Matrix<Double> triangleMatrix;
    private double determinant;

    private List<Double> solution;
    private List<Double> discrepancies;

    PivotAlgorithmResultBuilder() {
    }

    public PivotAlgorithmResultBuilder withSuccess(boolean success) {
      this.success = success;
      return this;
    }

    public PivotAlgorithmResultBuilder withErrorCode(int errorCode) {
      this.executionCode = errorCode;
      return this;
    }

    public PivotAlgorithmResultBuilder withTriangleMatrix(Matrix<Double> triangleMatrix) {
      this.triangleMatrix = new ListMatrix<>(triangleMatrix);
      return this;
    }

    public PivotAlgorithmResultBuilder withDeterminant(double determinant) {
      this.determinant = determinant;
      return this;
    }

    public PivotAlgorithmResultBuilder withSolution(List<Double> solution) {
      this.solution = new ArrayList<>(solution);
      return this;
    }

    public PivotAlgorithmResultBuilder withDiscrepancies(List<Double> discrepancies) {
      this.discrepancies = new ArrayList<>(discrepancies);
      return this;
    }

    @Override
    public PivotAlgorithmResult build() {
      return new PivotAlgorithmResult() {
        @Override
        public boolean isSuccessful() {
          return success;
        }

        @Override
        public int getErrorCode() {
          return executionCode;
        }

        @Override
        public Matrix<Double> getTriangleMatrix() {
          return triangleMatrix;
        }

        @Override
        public double getDeterminant() {
          return determinant;
        }

        @Override
        public List<Double> getSolution() {
          return solution;
        }

        @Override
        public List<Double> getDiscrepancies() {
          return discrepancies;
        }
      };
    }
  }
}