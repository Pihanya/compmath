package ru.pihanya.compmath.read;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface MatrixReader<T> {

  default MatrixReadResult<T> readSquareMatrix() {
    throw new NotImplementedException();
  }

  default MatrixReadResult<T> readMatrix() {
    throw new NotImplementedException();
  }

  default MatrixReadResult<T> readExactSquareMatrix() {
    throw new NotImplementedException();
  }

  default MatrixReadResult<T> readExactMatrix(int height, int width) {
    throw new NotImplementedException();
  }
}
