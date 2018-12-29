package ru.pihanya.compmath.integration;

import lombok.AllArgsConstructor;
import ru.pihanya.compmath.AlgorithmExecutor;
import ru.pihanya.compmath.MathFunction;

@AllArgsConstructor
public class IntegrationAlgorithmsTester {

  MathFunction antidetivative;
  AlgorithmExecutor<IntegrationAlgorithmArgument> executor;

  public boolean validate(IntegrationAlgorithmArgument argument) {
    double a = argument.getA();
    double b = argument.getB();
    double precision = argument.getPrecision();

    IntegrationAlgorithmResult result = (IntegrationAlgorithmResult) executor.execute(argument);

    double expectedIntegral = antidetivative.evaluate(b) - antidetivative.evaluate(a);
    double actualIntegral = result.getIntegrationResult();
    long iteration = result.getIterationsAmount();
    double error = result.getErrorValue();

    System.out.printf("Error code: %d\n", result.getErrorCode());
    System.out.printf("Actual integral: %.5f\n", actualIntegral);
    System.out.printf("Amount of iterations: %d\n", iteration);
    System.out.printf("Runge error: %.5f\n", error);
    System.out.println("---");
    System.out.printf("Expected integral: %.5f\n", expectedIntegral);
    System.out.printf("Asked precision: %.5f\n", precision);
    System.out.printf("Difference: %.5f\n", Math.abs(expectedIntegral - actualIntegral));
    System.out.println("===============================================");

    return (Math.abs(expectedIntegral - actualIntegral) < precision
        && result.getErrorValue() <= argument.getPrecision());
  }

  public boolean validate(MathFunction function,
      double a, double b, double precision,
      RectangleType type) {
    return validate(new IntegrationAlgorithmArgument(function, a, b, precision, type));
  }
}
