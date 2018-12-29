package ru.pihanya.compmath.integration;

import ru.pihanya.compmath.AlgorithmExecutor;
import ru.pihanya.compmath.MathFunction;
import ru.pihanya.compmath.utils.ConstantValues;

public class RectangleIntegrationAlgorithmExecutor implements
    AlgorithmExecutor<IntegrationAlgorithmArgument> {

  private static final long MAX_ITERATIONS_AMOUNT = Long.MAX_VALUE / 10;

  @Override
  public IntegrationAlgorithmResult execute(
      IntegrationAlgorithmArgument argument) {
    double a = argument.getA();
    double b = argument.getB();
    double precision = argument.getPrecision();
    RectangleType type = argument.getType();
    MathFunction function = argument.getFunction();

    if (Math.abs(b - a) <= ConstantValues.DOUBLE_MACHINE_EPSILON) {
      return getResultWithGiveData(function.evaluate(b), ConstantValues.DOUBLE_MACHINE_EPSILON, 1);
    }

    int n = 4;
    double integral, doubledNIntegal;
    double rungeCoefficient;
    while (true) {
      integral = countIntegral(function, a, b, n, type);
      doubledNIntegal = countIntegral(function, a, b, 2 * n, type);

      double h = countStep(a, b, 2 * n);
      rungeCoefficient = Math.abs(integral - doubledNIntegal) / (h - 1);
      if (Math.abs(rungeCoefficient) < precision) {
        break;
      } else if (n * 2 <= MAX_ITERATIONS_AMOUNT) {
        n *= 2;
      } else {
        return getResultWithErrorCode(2);
      }
    }

    return getResultWithGiveData(doubledNIntegal, rungeCoefficient, n * 2);
  }

  private static double countIntegral(MathFunction function,
      double a, double b, long n,
      RectangleType type) {
    double step = countStep(a, b, n);
    double left = getLeftBorder(a, step, type);
    double right = getRightBorder(b, step, type);
    double initialResult = getInitialResult(function, a, b);
    return executeItertaions(function, left, right, step, initialResult);
  }

  private static double countStep(double a, double b, long n) {
    return (b - a) / n;
  }

  private static double getInitialResult(MathFunction function, double a, double b) {
    return (function.evaluate(a) + function.evaluate(b)) / 2;
  }

  private static double getLeftBorder(double left, double step, RectangleType type) {
    switch (type) {
      case LEFT:
        return left;
      case CENTER:
        return left + step;
      case RIGHT:
        return left + step;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static double getRightBorder(double right, double step, RectangleType type) {
    switch (type) {
      case LEFT:
        return right - step;
      case CENTER:
        return right - step;
      case RIGHT:
        return right;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static double executeItertaions(MathFunction function,
      double leftBound, double rightBound, double step,
      double initialResult) {

    double result = initialResult;
    for (double x = leftBound; Math.abs(rightBound - x) > ConstantValues.DOUBLE_MACHINE_EPSILON; x += step) {
      result += function.evaluate(x);
    }

    result *= step;
    return result;
  }

  private IntegrationAlgorithmResult getResultWithGiveData(final double integrationResult,
      final double precision, final long n) {
    return new IntegrationAlgorithmResult() {
      @Override
      public double getIntegrationResult() {
        return integrationResult;
      }

      @Override
      public double getErrorValue() {
        return precision;
      }

      @Override
      public long getIterationsAmount() {
        return n;
      }
    };
  }

  private IntegrationAlgorithmResult getResultWithErrorCode(final int errorCode) {
    return new IntegrationAlgorithmResult() {
      @Override
      public int getErrorCode() {
        return errorCode;
      }
    };
  }
}
