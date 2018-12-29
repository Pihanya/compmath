package ru.pihanya.compmath.read;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ru.pihanya.compmath.math.ListMatrix;

public class ScannerMatrixReader<T> implements MatrixReader<T> {

  private Scanner scanner;
  private String delimiter;

  private ObjectMapper<T> mapper;

  public ScannerMatrixReader(Scanner scanner, String delimiter, ObjectMapper<T> mapper) {
    this.scanner = scanner;
    this.delimiter = delimiter;
    this.mapper = mapper;
  }

  @Override
  public MatrixReadResult<T> readSquareMatrix() {
    List<List<T>> matrixList;
    int n;
    try {
      String heightLine = scanner.nextLine().trim();

      n = new Scanner(new StringReader(heightLine)).nextInt();
      matrixList = new ArrayList<>(n + 1);

      for (int y = 0; y < n + 1; ++y) {
        String matrixLine = scanner.nextLine();
        Scanner scanner = new Scanner(new StringReader(matrixLine)).useDelimiter(delimiter);

        matrixList.add(new ArrayList<>(n));

        for (int x = 0; x < n; ++x) {
          matrixList.get(y).add(mapper.map(scanner.next()));
        }
      }
    } catch (Exception ex) {
      return new MatrixReadResult<T>()
          .withStatus(MatrixReadStatus.ERORR)
          .withException(ex);
    }

    return new MatrixReadResult<T>()
        .withMatrix(new ListMatrix<>(n, n, matrixList))
        .withStatus(MatrixReadStatus.SUCCESS);
  }
}
