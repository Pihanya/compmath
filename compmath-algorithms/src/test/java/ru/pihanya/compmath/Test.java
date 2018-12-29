package ru.pihanya.compmath;

import org.mariuszgromada.math.mxparser.Function;

public class Test {
  // ДЖАВА СКВИРТ
  /*public static void main(String[] args) throws ScriptException {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    String foo = "[1,2,3,4,5].reduce(function(acc, x) { return acc + x * x })";
    System.out.println(engine.eval(foo));
  }*/

  public static void main(String[] args) {
    Function function = new Function("f(x) = cos(x)" );
    System.out.println(function.calculate(1));
  }
}
