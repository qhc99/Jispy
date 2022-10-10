package dev.qhc99.jispy;

import dev.qhc99.interpreter.JispyInterpreter;

import java.io.File;

// TODO fix logic bug of cons
public class Jispy {
  public static void main(String[] args) {
    if (args.length == 2) {
      var f = new File(args[0]);
      var l = new File(args[1]);
      if (f.exists() && l.exists()) {
        long t1 = System.nanoTime();
        JispyInterpreter interpreter = new JispyInterpreter();
        interpreter.loadLib(l);
        interpreter.runFile(f);
        long t2 = System.nanoTime();
        System.out.printf("---run time: %fms---", (t2 - t1) / Math.pow(10, 6));
      }
    }
    else if(args.length == 1) {
      var f = new File(args[0]);
      if (f.exists()) {
        JispyInterpreter interpreter = new JispyInterpreter();
        interpreter.runFile(f);
      }
    }
    else {
      JispyInterpreter interpreter = new JispyInterpreter();
      interpreter.repl();
    }
  }
}
