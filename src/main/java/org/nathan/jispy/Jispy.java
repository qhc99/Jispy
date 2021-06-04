package org.nathan.jispy;

import org.nathan.interpreter.JispyInterpreter;

import java.io.*;

public class Jispy{
    public static void main(String[] args){
        JispyApp(args);
    }

    static void JispyApp(String[] args){
        if(args.length == 2){
            long t1 = System.nanoTime();
            var f = new File(args[0]);
            var l = new File(args[1]);
            if(f.exists() && l.exists()){
                JispyInterpreter interpreter = new JispyInterpreter();

                int i = 0;
                while(i < 1000){
                    interpreter.loadLib(l);
                    interpreter.runFile(f);
                    i++;
                }

            }
            long t2 = System.nanoTime();
            System.out.printf("---run time: %fms---", (t2 - t1) / Math.pow(10, 6));
        }
        else{
            JispyInterpreter interpreter = new JispyInterpreter();
            interpreter.repl();
        }
    }
}
