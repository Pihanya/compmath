package ru.pihanya.compmath.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class ListMatrix<T> implements ImmutableMatrix<T>, Cloneable {

  private int height;
  private int width;

  private List<List<T>> elements;

  public ListMatrix() {
    this(0, 0, Collections.emptyList());
  }

  public ListMatrix(Matrix<T> matrix) {
    this.height = matrix.getHeight();
    this.width = matrix.getWidth();

    this.elements = new ArrayList<>(height);
    for (int y = 0; y < height; ++y) {
      elements.add(new ArrayList<>(width));
      for (int x = 0; x < width; ++x) {
        elements.get(y).add(matrix.get(y + 1, x + 1));
      }
    }
  }

  public ListMatrix(int height, int width, T[][] values) {
    this.height = height;
    this.width = width;
    this.elements = new ArrayList<>(height);

    for (int y = 0; y < height; ++y) {
      elements.add(Arrays.asList(values[y]).subList(0, width));
    }
  }

  public ListMatrix(int height, int width, List<List<T>> elements) {
    this.height = height;
    this.width = width;
    this.elements = new ArrayList<>(elements);
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public T get(int y, int x) {
    if (x > width || y > height) {
      throw new IllegalArgumentException(
          String.format(
              "Wrong element index [%d, %d] for matrix with bounds [%d, %d]",
              y,
              x,
              getHeight(),
              getWidth()));
    }

    return elements.get(y - 1).get(x - 1);
  }

  @Override
  public List<T> getRow(int y) {
    return new ArrayList<>(elements.get(y - 1));
  }

  @Override
  public List<T> getColumn(int x) {
    List<T> column = new ArrayList<>(height);
    for (int y = 0; y < height; ++y) {
      column.add(elements.get(y).get(x - 1));
    }
    return column;
  }

  @Override
  public ImmutableMatrix<T> set(int y, int x, T element) {
    List<List<T>> elements = new ArrayList<>(this.elements);
    elements.get(y - 1).set(x - 1, element);
    return new ListMatrix<>(height, width, elements);
  }

  @Override
  public ImmutableMatrix<T> swapRows(int y1, int y2) {
    List<List<T>> elements = new ArrayList<>(this.elements);
    for (int x = 0; x < width; ++x) {
      T swap = elements.get(y1 - 1).get(x);
      elements.get(y1 - 1).set(x, elements.get(y2 - 1).get(x));
      elements.get(y2 - 1).set(x, swap);
    }
    return new ListMatrix<>(height, width, elements);
  }

  public ImmutableMatrix<T> swapColumns(int x1, int x2) {
    List<List<T>> elements = new ArrayList<>(this.elements);
    for (int y = 0; y < height; ++y) {
      T swap = elements.get(x1 - 1).get(y);
      elements.get(x1 - 1).set(y, elements.get(x2 - 1).get(y));
      elements.get(x2 - 1).set(y, swap);
    }
    return new ListMatrix<>(height, width, elements);
  }

  @Override
  public ImmutableMatrix<T> addRow(int rowNumber, List<T> row) {
    if (height == 0) {
      return new ListMatrix<>(1, row.size(), new ArrayList<List<T>>() {{
        add(row);
      }});
    }

    List<List<T>> elements = new ArrayList<>(this.elements);
    elements.add(rowNumber - 1, row);
    return new ListMatrix<>(height + 1, width, elements);
  }

  @Override
  public ImmutableMatrix<T> addColumn(int insertIndex, List<T> column) {
    if (width == 0) {
      return new ListMatrix<>(column.size(), 1, new ArrayList<List<T>>() {{
        for (T value : column) {
          add(Collections.singletonList(value));
        }
      }});
    }

    List<List<T>> elements = new ArrayList<>(this.elements);
    for (int y = 0; y < height; ++y) {
      elements.get(y).add(insertIndex - 1, column.get(y));
    }
    return new ListMatrix<>(height, width + 1, elements);
  }

  @Override
  public ImmutableMatrix<T> removeRow(int y) {
    List<List<T>> elements = new ArrayList<>(height - 1);
    for (int i = 0; i < height; ++i) {
      if (i != (y - 1)) {
        elements.add(this.elements.get(i));
      }
    }
    return new ListMatrix<>(height - 1, width, elements);
  }

  @Override
  public ImmutableMatrix<T> removeColumn(int x) {
    List<List<T>> elements = new ArrayList<>(height);
    for (int i = 0; i < height; ++i) {
      ArrayList<T> rowList = new ArrayList<>(width - 1);
      for (int j = 0; j < width; ++j) {
        if (j != (x - 1)) {
          rowList.add(this.elements.get(i).get(j));
        }
      }
      elements.add(rowList);
    }
    return new ListMatrix<>(height - 1, width, elements);
  }

  public T[][] toArray(BiFunction<Integer, Integer, T[][]> generator) {
    T[][] array = generator.apply(height, width);

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        array[y][x] = elements.get(y).get(x);
      }
    }

    return array;
  }

  @Override
  public String toString() {
    StringBuilder representation = new StringBuilder();

    for (int y = 1; y <= height; y++) {
      representation.append(String.join(" ",
          getRow(y).stream()
              .map(Object::toString)
              .collect(Collectors.toList())));

      if (y == height) {
        continue;
      }

      representation.append('\n');
    }

    return representation.toString();
  }
}
