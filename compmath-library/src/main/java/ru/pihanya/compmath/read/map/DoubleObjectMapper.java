package ru.pihanya.compmath.read.map;

import ru.pihanya.compmath.read.ObjectMapper;

public class DoubleObjectMapper implements ObjectMapper<Double> {

  @Override
  public Double map(String piece) {
    return Double.parseDouble(piece);
  }
}
