package ru.pihanya.compmath.pivot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import ru.pihanya.compmath.AlgorithmExecutor;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;
import ru.pihanya.compmath.math.Matrix;
import ru.pihanya.compmath.utils.MatrixUtils;

public class GaussianPivotAlgorithmExecutor implements AlgorithmExecutor {

  private static final boolean DEBUG = true;

  private final PivotSearchMethod pivotSearchMethod;

  private ImmutableMatrix<Double> sourceExtendedMatrix;


  public GaussianPivotAlgorithmExecutor(Matrix<Double> extendedMatrix,
      PivotSearchMethod pivotSearchMethod) {
    this.sourceExtendedMatrix = new ListMatrix<>(extendedMatrix);
    this.pivotSearchMethod = pivotSearchMethod;
  }

  @Override
  public PivotAlgorithmResult execute() {
    ImmutableMatrix<Double> matrix = new ListMatrix<>(sourceExtendedMatrix);

    for (int k = 1; k < matrix.getHeight(); ++k) {
      if (DEBUG) {
        printMatrix(matrix);
      }
      int pivotY = findLargestPivot(matrix, k);
      if (pivotY > k) {
        matrix = matrix.swapRows(k, pivotY);
        pivotY = k;

        if (DEBUG) {
          System.out.printf("Swapping rows: %d and %d\n", k, pivotY);
        }
      }

      if (pivotY == -1) {
        System.out.println("All elements have zero value");
        throw new IllegalArgumentException("All elements of given matrix are zero");
      }

      for (int y = k + 1; y <= matrix.getHeight(); ++y) {
        double m = -(matrix.get(y, k) / matrix.get(pivotY, k));

//        System.out.println("m = -A(y, k)/A(p, q)");
        System.out.printf("m = -A(%d, %d)/A(%d, %d)=%f\n", y, k, pivotY, k, m);
        for (int x = 1; x <= matrix.getWidth(); ++x) {
          double elem = matrix.get(y, x);
          double elemFromPivotRow = matrix.get(pivotY, x);
          elem = elem + m * elemFromPivotRow;
          matrix = matrix.set(y, x, elem);

          if (DEBUG) {
//            System.out.print("A(y, x) = A(y, x) + m * A(q, x)");
            System.out.printf("A(%d, %d) = A(%d, %d) + %f * A(%d, %d) = %f\n", y, x, y, x, m, pivotY, x, elem);
          }
        }
      }
    }

    ImmutableMatrix<Double> finalMatrix = matrix;
    printMatrix(finalMatrix);

    double determinant = countDeterminant(matrix);
    System.out.printf("Determinant: %f\n", determinant);

    List<Double> xs = new ArrayList<>(matrix.getWidth() - 1);
    for (int i = 0; i < matrix.getHeight(); ++i, xs.add(0D)) {
    }

    for (int y = matrix.getHeight(); y >= 1; --y) {
      double ans = matrix.get(y, matrix.getWidth());
      int x = matrix.getHeight();
      for (; x > y; --x) {
        ans -= (xs.get(x - 1) * matrix.get(y, x));
      }
      xs.set(x - 1, ans / matrix.get(y, x));
    }

    System.out.printf("Solution: %s\n", xs.toString());

    List<Double> discrepancies = countDiscrepancies(sourceExtendedMatrix, xs);
    System.out.printf("Discrepancies: %s\n", discrepancies);
    return new PivotAlgorithmResult() {
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

      @Override
      public boolean isSuccessful() {
        return true;
      }

      @Override
      public int getErrorCode() {
        return 0;
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
    for (int y = col; y <= extendedMatrix.getHeight(); ++y) {
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

  private void printMatrix(Matrix<Double> matrix) {
    String header = String.format("=========================[%d, %d]=========================",
        matrix.getHeight(), matrix.getWidth());

    System.out.println(header);
    for (int y = 1; y <= matrix.getHeight(); ++y) {
      for (int x = 1; x <= matrix.getWidth(); ++x) {
        System.out.printf("%-15.5f", matrix.get(y, x));
      }
      System.out.println();
    }
    System.out.println(Stream.generate(() -> "=")
        .limit(header.length())
        .reduce((s, s2) -> s + s2)
        .get());
    System.out.println();
  }

  public static void help() {
    System.out.println("-f {filename} - read matrix from the file");
    System.out.println("-i - read matrix from the output");
    System.out.println("-r {n} - create random matrix");
  }

  public static void main(String[] args) throws FileNotFoundException {
    if (args.length == 0) {
      System.out.println("Program has no args. Please consider using some command line arguments");
      help();
    }

    List<List<Double>> matrixList;
    PivotAlgorithmResult result;
    Scanner scanner;
    int n;
    switch (args[0]) {
      case "-f":
        if (args.length == 1) {
          System.out.println("Program has no file name to read matrix from.");
          help();
        }

        scanner = new Scanner(new FileInputStream(args[1]));
        n = scanner.nextInt();
        matrixList = new ArrayList<>(n + 1);
        for (int y = 0; y < n + 1; ++y) {
          matrixList.add(new ArrayList<>(n));
          for (int x = 0; x < n; ++x) {
            matrixList.get(y).add(scanner.nextDouble());
          }
        }

        break;

      case "-i":

        scanner = new Scanner(System.in);
        n = scanner.nextInt();
        matrixList = new ArrayList<>(n + 1);
        for (int y = 0; y < n + 1; ++y) {
          matrixList.add(new ArrayList<>(n));
          for (int x = 0; x < n; ++x) {
            matrixList.get(y).add(scanner.nextDouble());
          }
        }

        break;

      case "-r":
        final int bound = 100;
        Matrix<Double> randomMatrix = MatrixUtils.getRandomMatrix(10, 11, bound);
        result = new GaussianPivotAlgorithmExecutor(randomMatrix, PivotSearchMethod.BOTH).execute();
    }
  }
}
