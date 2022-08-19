package org.qhc99.jispy;

import org.qhc99.interpreter.JispyInterpreter;

import java.io.File;

// TODO fix logic bug of cons
public class Jispy{
  public static void main(String[] args){
    JispyApp(args);
  }

  static void JispyApp(String[] args){
    if(args.length == 2){
      var f = new File(args[0]);
      var l = new File(args[1]);
      if(f.exists() && l.exists()){
        long t1 = System.nanoTime();
        JispyInterpreter interpreter = new JispyInterpreter();
        interpreter.loadLib(l);
        interpreter.runFile(f);
        long t2 = System.nanoTime();
        System.out.printf("---run time: %fms---", (t2 - t1) / Math.pow(10, 6));
      }
    }
    else{
      JispyInterpreter interpreter = new JispyInterpreter();
      interpreter.repl();
    }
  }
}
