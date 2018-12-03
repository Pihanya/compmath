package ru.pihanya.compmath.math;

import java.util.List;

public interface ImmutableMatrix<T> extends Matrix<T> {

  ImmutableMatrix<T> removeRow(int y);

  ImmutableMatrix<T> removeColumn(int x);

  ImmutableMatrix<T> set(int y, int x, T element);

  ImmutableMatrix<T> swapRows(int y1, int y2);

  ImmutableMatrix<T> swapColumns(int y1, int y2);

  ImmutableMatrix<T> addRow(int insertIndex, List<T> row);

  default ImmutableMatrix<T> addRow(List<T> row) {
    return addRow(getHeight() + 1, row);
  }

  ImmutableMatrix<T> addColumn(int insertIndex, List<T> column);

  default ImmutableMatrix<T> addColumn(List<T> column) {
    return addColumn(getWidth() + 1, column);
  }
}