package ru.pihanya.compmath.read;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import ru.pihanya.compmath.math.Matrix;

@Getter
@AllArgsConstructor
@Wither
@NoArgsConstructor
public class MatrixReadResult<T> {

  private MatrixReadStatus status;
  private Matrix<T> matrix;
  private Exception exception;

  public boolean isSuccess() {
    return status == MatrixReadStatus.SUCCESS;
  }
}
