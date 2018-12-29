package ru.pihanya.compmath.scripts;

import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.COMMAND_PARSER;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.EXIT_COMMAND;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.HELP_COMMAND;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_COMMAND;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_EQUATION_ARGUMENT;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_HIGHER_BORDER_ARGUMENT;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_LOWER_BORDER_ARGUMENT;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_PARSER;
import static ru.pihanya.compmath.scripts.RectangleIntegrationAlgorithmScript.ArgumentInfos.INTEGRATE_PRECISION_ARGUMENT;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import org.mariuszgromada.math.mxparser.Function;
import ru.pihanya.compmath.integration.IntegrationAlgorithmArgument;
import ru.pihanya.compmath.integration.IntegrationAlgorithmResult;
import ru.pihanya.compmath.integration.RectangleIntegrationAlgorithmExecutor;
import ru.pihanya.opensorce.Argument;
import ru.pihanya.opensorce.ArgumentInfo;
import ru.pihanya.opensorce.parsing.ArgumentParser;
import ru.pihanya.opensorce.parsing.ParseResult;
import ru.pihanya.opensorce.parsing.realisations.SimpleArgumentParser;

public class RectangleIntegrationAlgorithmScript {

  private static final double DEFAULT_PRECISION = 0.01D;

  public static void main(String[] args) throws InterruptedException {
    Scanner scanner = new Scanner(System.in);

    String line;
    while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
      ParseResult command = COMMAND_PARSER.parse(line);

      if (command.getData().containsKey(INTEGRATE_COMMAND)) {
        ParseResult arguments = INTEGRATE_PARSER.parse(line);

        String equation;
        double lowerBorder, higherBorder;
        double precision;

        Map<ArgumentInfo, Argument> data = arguments.getData();

        if (data.containsKey(INTEGRATE_EQUATION_ARGUMENT)) {
          equation = data.get(INTEGRATE_EQUATION_ARGUMENT).getValue();

          if (equation == null) {
            System.err.println("Equation cannot be null");
            continue;
          } else {
            System.out.println("Function: " + equation);
          }
        } else {
          System.err.println("No equation argument!");
          printHelp();
          continue;
        }

        if (data.containsKey(INTEGRATE_LOWER_BORDER_ARGUMENT)) {
          lowerBorder = Double.parseDouble(data.get(INTEGRATE_LOWER_BORDER_ARGUMENT).getValue());
        } else {
          System.err.println("No lower border for integral!");
          printHelp();
          continue;
        }

        if (data.containsKey(INTEGRATE_HIGHER_BORDER_ARGUMENT)) {
          higherBorder = Double.parseDouble(data.get(INTEGRATE_HIGHER_BORDER_ARGUMENT).getValue());
        } else {
          System.err.println("No higher border for integral!");
          printHelp();
          continue;
        }

        if (data.containsKey(INTEGRATE_PRECISION_ARGUMENT)) {
          precision = Double.parseDouble(data.get(INTEGRATE_PRECISION_ARGUMENT).getValue());
        } else {
          System.out.println("Setting precision to default value (" + DEFAULT_PRECISION + ")");
          precision = DEFAULT_PRECISION;
        }

        Function function = new Function(equation);
        RectangleIntegrationAlgorithmExecutor executor = new RectangleIntegrationAlgorithmExecutor();
        IntegrationAlgorithmResult result = executor.execute(
            new IntegrationAlgorithmArgument(
                function::calculate, lowerBorder, higherBorder, precision
            ));

        if (!result.isSuccessful()) {
          System.out.println(
              "Algorithm execution was failed with error code (" + result.getErrorValue() + ")");
        } else {
          double integrationResult = result.getIntegrationResult();
          long iterationsAmount = result.getIterationsAmount();
          double errorValue = result.getErrorValue();

          System.out.println("Integration result: " + integrationResult);
          System.out.println("Iterations amount: " + iterationsAmount);
          System.out.println("Error value: " + errorValue);
        }
      } else if (command.getData().containsKey(HELP_COMMAND)) {
        printHelp();
      } else if (command.getData().containsKey(EXIT_COMMAND)) {
        System.out.println("Exiting...");
        Thread.sleep(1000L);
        System.exit(0);
      }

      Thread.sleep(300L);
    }
  }

  private static void printHelp() {
    System.out.println(
        "integrate -f {function} -l {left} -r {right} - integrate {function} from {left} to {right}");
    System.out.println("exit - exit program");
  }

  public static class ArgumentInfos {

    /*==================== INTEGRATE ====================*/
    public static final ArgumentInfo INTEGRATE_COMMAND = new ArgumentInfo("integrate",
        Arrays.asList("integrate", "int"));

    public static final ArgumentInfo INTEGRATE_EQUATION_ARGUMENT =
        new ArgumentInfo("function", Arrays.asList("function", "f", "equation", "eq", "e"));
    public static final ArgumentInfo INTEGRATE_LOWER_BORDER_ARGUMENT =
        new ArgumentInfo("lower_border", Arrays.asList("lower", "left", "l", "a"));
    public static final ArgumentInfo INTEGRATE_HIGHER_BORDER_ARGUMENT =
        new ArgumentInfo("higher_border", Arrays.asList("higher", "right", "h", "b"));
    public static final ArgumentInfo INTEGRATE_PRECISION_ARGUMENT =
        new ArgumentInfo("precision", Arrays.asList("precision", "p"));

    /*==================== EXIT ====================*/
    public static final ArgumentInfo EXIT_COMMAND =
        new ArgumentInfo("exit", Arrays.asList("exit", "quit", "q"), "Exit program");

    /*==================== EXIT ====================*/
    public static final ArgumentInfo HELP_COMMAND =
        new ArgumentInfo("help", Arrays.asList("help", "h"), "Exit program");

    public static final ArgumentParser INTEGRATE_PARSER = new SimpleArgumentParser(
        () -> Arrays.asList(INTEGRATE_EQUATION_ARGUMENT,
            INTEGRATE_HIGHER_BORDER_ARGUMENT,
            INTEGRATE_LOWER_BORDER_ARGUMENT));

    public static final ArgumentParser COMMAND_PARSER = new SimpleArgumentParser(
        () -> Arrays.asList(INTEGRATE_COMMAND, EXIT_COMMAND));
  }
}
