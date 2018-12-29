package ru.pihanya.compmath.scripts;

import static ru.pihanya.compmath.scripts.GaussianPivotAlgorithmScript.ArgumentInfos.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;
import ru.pihanya.compmath.equations.GaussianAlgorithmResult;
import ru.pihanya.compmath.equations.GaussianPivotAlgorithmExecutor;
import ru.pihanya.compmath.math.ImmutableMatrix;
import ru.pihanya.compmath.math.ListMatrix;
import ru.pihanya.compmath.math.Matrix;
import ru.pihanya.compmath.utils.MatrixUtils;
import ru.pihanya.opensorce.Argument;
import ru.pihanya.opensorce.ArgumentInfo;
import ru.pihanya.opensorce.parsing.ArgumentParser;
import ru.pihanya.opensorce.parsing.ParseResult;
import ru.pihanya.opensorce.parsing.realisations.CommandParser;
import ru.pihanya.opensorce.parsing.realisations.SimpleArgumentParser;

public class GaussianPivotAlgorithmScript {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    String input;
    Scanner scanner = new Scanner(System.in);
    Matrix<Double> matrix = null;
    int height, width;

    while ((input = scanner.nextLine()) != null && !input.isEmpty()) {
      ParseResult commandParseResult = COMMAND_PARSER.parse(input);
      if (!commandParseResult.isSuccess()) {
        System.err.printf("Unknown command: %s\n", input);
        help();
        continue;
      }

      Map<ArgumentInfo, Argument> commandData = commandParseResult.getData();
      if (commandData.containsKey(RANDOM_COMMAND)) {
        ParseResult arguments = RANDOM_COMMAND_ARGUMENT_PARSER.parse(input);
        Map<ArgumentInfo, Argument> argumentsData = arguments.getData();
        if (argumentsData.containsKey(RANDOM_MATRIX_SIZE_ARGUMENT)) {
          height = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_SIZE_ARGUMENT).getValue());
          width = height + 1;
          matrix = generateRandomMatrix(height, width);
        } else if (argumentsData.containsKey(RANDOM_MATRIX_HEIGHT_ARGUMENT)
            && argumentsData.containsKey(RANDOM_MATRIX_WIDTH_ARGUMENT)) {
          height = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_HEIGHT_ARGUMENT).getValue());
          width = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_WIDTH_ARGUMENT).getValue());
          matrix = generateRandomMatrix(height, width);
        }
      } else if (commandData.containsKey(READ_COMMAND)) {
        ParseResult arguments = READ_COMMAND_ARGUMENT_PARSER.parse(input);
        Map<ArgumentInfo, Argument> argumentsData = arguments.getData();
        if (argumentsData.containsKey(RANDOM_MATRIX_SIZE_ARGUMENT)) {
          height = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_SIZE_ARGUMENT).getValue());
          matrix = readMatrix(scanner, height, height + 1);
        } else if (argumentsData.containsKey(RANDOM_MATRIX_HEIGHT_ARGUMENT)
            && argumentsData.containsKey(RANDOM_MATRIX_WIDTH_ARGUMENT)) {
          height = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_HEIGHT_ARGUMENT).getValue());
          width = Integer
              .parseInt(argumentsData.get(RANDOM_MATRIX_WIDTH_ARGUMENT).getValue());
          matrix = readMatrix(scanner, height, width);
        } else if (argumentsData.containsKey(READ_INPUT_FILE_NAME)) {
          String fileName = argumentsData.get(READ_INPUT_FILE_NAME).getValue();
          if (fileName == null) {
            System.err
                .println("File name was not consumed. Try again with defining source file name.");
            continue;
          }

          if (!Files.exists(Paths.get(fileName))) {
            System.err.printf("File with name \"%s\" does not exist!\n", fileName);
            continue;
          }

          Scanner fileReader = new Scanner(new FileReader(new File(fileName)));
          matrix = readMatrix(fileReader, null, null);
        }
      } else if (commandData.containsKey(HELP_COMMAND)) {
        help();
        continue;
      } else if (commandData.containsKey(EXIT_COMMAND)) {
        System.out.println("Exiting program");
        Thread.sleep(1000L);
        System.exit(0);
      }

      if (matrix == null) {
        System.err.println("Matrix could not be read. Try again please!");
        continue;
      } else {
        System.out.println("Read matrix:");
        printMatrix(matrix);
      }

      GaussianPivotAlgorithmExecutor executor = new GaussianPivotAlgorithmExecutor();
      GaussianAlgorithmResult algorithmResult = executor.execute((matrix));

      if (!algorithmResult.isSuccessful()) {
        System.err
            .printf("Algorihtm execution failed. Error code: %d\n", algorithmResult.getErrorCode());
        continue;
      }

      printMatrix(algorithmResult.getTriangleMatrix());
      System.out.printf("Determinant: %.3f\n", algorithmResult.getDeterminant());

      for (int i = 0; i < algorithmResult.getSolution().size(); ++i) {
        System.out.printf("x%d: %10.3f\n", (i + 1), algorithmResult.getSolution().get(i));
      }

      System.out.println("Discrepancies:");
      for (int i = 0; i < algorithmResult.getDiscrepancies().size(); ++i) {
        System.out.printf("%d: %.10f\n", (i + 1), algorithmResult.getDiscrepancies().get(i));
      }

      Thread.sleep(1000L);
    }
  }

  private static Matrix<Double> readMatrix(Scanner input, Integer height, Integer width) {
    try {
      if (height == null && width == null) {
        if (!input.hasNextLine()) {
          return null;
        }

        Scanner sizesReader = new Scanner(new StringReader(input.nextLine()));
        if (sizesReader.hasNextInt()) {
          height = sizesReader.nextInt();
        }

        if (sizesReader.hasNextInt()) {
          width = sizesReader.nextInt();
        } else {
          width = height + 1;
        }
      } else if (height == null || width == null) {
        return null;
      }

      List<List<Double>> matrixList = new ArrayList<>(height);

      for (int y = 0; y < height; ++y) {
        if (!input.hasNextLine()) {
          return null;
        }

        String line = input.nextLine();
        Scanner rowScanner = new Scanner(new StringReader(line));
        rowScanner.useDelimiter("[\t ]+");
        List<Double> matrixRow = new ArrayList<>(width);
        for (int x = 0; x < width; ++x) {
          try {
            if (!rowScanner.hasNextDouble()) {
              return null;
            }

            matrixRow.add(rowScanner.nextDouble());
          } catch (Exception ex) {
            return null;
          }
        }

        matrixList.add(matrixRow);
      }

      return new ListMatrix<>(height, width, matrixList);
    } catch (Exception ex) {
      return null;
    }
  }

  private static void printMatrix(Matrix<Double> matrix) {
    String header = String.format("=========================[%d, %d]=========================",
        matrix.getHeight(), matrix.getWidth());

    System.out.println(header);
    for (int y = 1; y <= matrix.getHeight(); ++y) {
      for (int x = 1; x <= matrix.getWidth(); ++x) {
        System.out.printf("%-10.3f", matrix.get(y, x));
      }
      System.out.println();
    }
    System.out.println(Stream.generate(() -> "=")
        .limit(header.length())
        .reduce((s, s2) -> s + s2)
        .get());
    System.out.println();
  }

  public static void help() {
    System.out.println("read [-f {filename}] [-h {height} -w {width}] - read matrix from the file");
    System.out.println("random -h {height} -w {width}");
    System.out.println("help - get help");
    System.out.println("exit - get help");
  }

  public static ImmutableMatrix<Double> generateRandomMatrix(int height, int width) {
    return new ListMatrix<>(
        MatrixUtils.getRandomMatrix(height, width, 50));
  }

  public static class ArgumentInfos {

    /*==================== RANDOM ====================*/
    public static final ArgumentInfo RANDOM_COMMAND =
        new ArgumentInfo("random", Arrays.asList("random", "rnd"),
            "Use generateRandomMatrix generated matrix");

    public static final ArgumentInfo RANDOM_MATRIX_SIZE_ARGUMENT =
        new ArgumentInfo("size", Arrays.asList("size", "n"),
            "Random matrix size (in case if squared matrix is required)");
    public static final ArgumentInfo RANDOM_MATRIX_WIDTH_ARGUMENT =
        new ArgumentInfo("width", Arrays.asList("width", "w"), "Random matrix width");
    public static final ArgumentInfo RANDOM_MATRIX_HEIGHT_ARGUMENT =
        new ArgumentInfo("height", Arrays.asList("height", "h"), "Random matrix height");;

    public static final ArgumentParser RANDOM_COMMAND_ARGUMENT_PARSER = new SimpleArgumentParser(
        () -> Arrays.asList(
            RANDOM_MATRIX_HEIGHT_ARGUMENT,
            RANDOM_MATRIX_WIDTH_ARGUMENT,
            RANDOM_MATRIX_SIZE_ARGUMENT
        )
    );

    /*==================== READ ====================*/
    public static final ArgumentInfo READ_COMMAND = new ArgumentInfo("read",
        Arrays.asList("read", "r"), "Read matrix");

    public static final ArgumentInfo READ_INPUT_FILE_NAME =
        new ArgumentInfo("name", Arrays.asList("f", "file"), "Name of file to read from");
    public static final ArgumentInfo READ_MATRIX_SIZE_ARGUMENT =
        new ArgumentInfo("size", Arrays.asList("size", "n"),
            "Random matrix size (in case if squared matrix is required)");
    public static final ArgumentInfo READ_MATRIX_WIDTH_ARGUMENT =
        new ArgumentInfo("width", Arrays.asList("width", "w"), "Random matrix width");
    public static final ArgumentInfo READ_MATRIX_HEIGHT_ARGUMENT =
        new ArgumentInfo("height", Arrays.asList("height", "h"), "Random matrix height");


    public static final ArgumentParser READ_COMMAND_ARGUMENT_PARSER = new SimpleArgumentParser(
        () -> Arrays.asList(
            READ_INPUT_FILE_NAME,
            READ_MATRIX_SIZE_ARGUMENT,
            READ_MATRIX_WIDTH_ARGUMENT,
            READ_MATRIX_HEIGHT_ARGUMENT
        )
    );

    /*==================== HELP ====================*/
    public static final ArgumentInfo HELP_COMMAND = new ArgumentInfo("help",
        Arrays.asList("help", "h"), "Show commands help");


    /*==================== EXIT ====================*/
    public static final ArgumentInfo EXIT_COMMAND =
        new ArgumentInfo("exit", Arrays.asList("exit", "quit", "q"), "Exit program");

    /*==================== COMMAND_PARSER ====================*/
    public static final CommandParser COMMAND_PARSER = new CommandParser(() ->
        Arrays.asList(READ_COMMAND, RANDOM_COMMAND, HELP_COMMAND, EXIT_COMMAND));

    /*==================== ARGUMENT_PARSER ====================*/
    private static final ArgumentParser readCommandArgumentParser =
        new SimpleArgumentParser(
            () -> Arrays.asList(RANDOM_MATRIX_SIZE_ARGUMENT,
                RANDOM_MATRIX_HEIGHT_ARGUMENT, RANDOM_MATRIX_WIDTH_ARGUMENT,
                READ_INPUT_FILE_NAME));
  }
}
