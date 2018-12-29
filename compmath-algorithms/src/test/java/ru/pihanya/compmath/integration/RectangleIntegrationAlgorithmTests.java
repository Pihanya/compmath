package ru.pihanya.compmath.integration;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RectangleIntegrationAlgorithmTests {

  private RectangleIntegrationAlgorithmExecutor executor;
  private final double a = 0D;
  private final double b = 1D;
  private final double precision = 0.001D;

  @BeforeEach
  public void init() {
    executor = new RectangleIntegrationAlgorithmExecutor();
  }

  @Test
  public void integrateX() {
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x / 2, executor)
        .validate((x) -> x, a, b, precision, RectangleType.LEFT));
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x / 2, executor)
        .validate((x) -> x, a, b, precision, RectangleType.CENTER));
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x / 2, executor)
        .validate((x) -> x, a, b, precision, RectangleType.RIGHT));
  }

  @Test
  public void integrateSquaredX() {
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x * x / 3, executor)
        .validate((x) -> x * x, a, b, precision, RectangleType.LEFT));
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x * x / 3, executor)
        .validate((x) -> x * x, a, b, precision, RectangleType.CENTER));
    assertTrue(new IntegrationAlgorithmsTester((x) -> x * x * x / 3, executor)
        .validate((x) -> x * x, a, b, precision, RectangleType.RIGHT));
  }

  @Test
  public void integrateCosX() {
    assertTrue(new IntegrationAlgorithmsTester(Math::sin, executor)
        .validate(Math::cos, a, b, precision, RectangleType.LEFT));
    assertTrue(new IntegrationAlgorithmsTester(Math::sin, executor)
        .validate(Math::cos, a, b, precision, RectangleType.CENTER));
    assertTrue(new IntegrationAlgorithmsTester(Math::sin, executor)
        .validate(Math::cos, a, b, precision, RectangleType.RIGHT));
  }

  @Test
  public void integrateEPoweredMinusXPowered2() {
    IntegrationAlgorithmResult execute = executor
        .execute(new IntegrationAlgorithmArgument((x) -> Math.exp(-x * x), a, b, precision));

    System.out.println(execute.getIntegrationResult());
    System.out.println(execute.getErrorValue());
    System.out.println(execute.getIterationsAmount());
  }

  @Test
  public void integrateSqrtX() {
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> ((double) 2 / (3) * Math.pow(x, 1.5)), executor)
            .validate(Math::sqrt, a, b, precision, RectangleType.LEFT));
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> ((double) 2 / (3) * Math.pow(x, 1.5)), executor)
            .validate(Math::sqrt, a, b, precision, RectangleType.CENTER));
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> ((double) 2 / (3) * Math.pow(x, 1.5)), executor)
            .validate(Math::sqrt, a, b, precision, RectangleType.RIGHT));
  }

  @Test
  public void integrateReciprocalX() {
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> log(x) / log(Math.E), executor)
            .validate((x) -> (1 / x), a, b, precision, RectangleType.LEFT));
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> log(x) / log(Math.E), executor)
            .validate((x) -> (1 / x), a, b, precision, RectangleType.CENTER));
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> log(x) / log(Math.E), executor)
            .validate((x) -> (1 / x), a, b, precision, RectangleType.RIGHT));
  }

  @Test
  public void integrateReciprocalRootedX() {
    assertTrue(
        new IntegrationAlgorithmsTester((x) -> (2 * sqrt(x)), executor)
            .validate((x) -> 1 / sqrt(x), a, b, precision, RectangleType.LEFT));

    assertTrue(
        new IntegrationAlgorithmsTester((x) -> (2 * sqrt(x)), executor)
            .validate((x) -> 1 / sqrt(x), a, b, precision, RectangleType.CENTER));

    assertTrue(
        new IntegrationAlgorithmsTester((x) -> (2 * sqrt(x)), executor)
            .validate((x) -> 1 / sqrt(x), a, b, precision, RectangleType.RIGHT));
  }
}
