package ru.pihanya.compmath.integration;

import java.util.Objects;
import lombok.Getter;
import ru.pihanya.compmath.MathFunction;
import ru.pihanya.compmath.utils.ConstantValues;

@Getter
public class IntegrationAlgorithmArgument {

  private MathFunction function;

  private double a, b;
  private double precision;
  private RectangleType type;

  public IntegrationAlgorithmArgument(MathFunction function, double a, double b, double precision,
      RectangleType type) {
    Objects.requireNonNull(function, "Function cannot be null");
    Objects.requireNonNull(type, "Rectangle type cannot be null");

    if (Double.isNaN(a)
        || Double.isNaN(b)
        || Double.isNaN(precision)) {
      throw new IllegalArgumentException("Arguments cannot be NaN");
    }

    if (Double.isInfinite(a)
        || Double.isInfinite(b)
        || Double.isInfinite(precision)) {
      throw new IllegalArgumentException("Arguments should be finite");
    }

    if (precision <= ConstantValues.DOUBLE_MACHINE_EPSILON) {
      throw new IllegalArgumentException("Algorithm step should be greater than zero");
    }

    this.function = function;

    this.a = Math.min(a, b);
    this.b = Math.max(a, b);

    this.precision = precision;
    this.type = type;
  }

  public IntegrationAlgorithmArgument(MathFunction function, double a, double b,
      double precision) {
    this(function, a, b, precision, RectangleType.CENTER);
  }
}
