package ru.pihanya.compmath.math;

import java.util.List;

public interface Matrix<T> {

  int getHeight();

  int getWidth();

  T get(int y, int x);

  List<T> getColumn(int x);

  List<T> getRow(int y);
}
