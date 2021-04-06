package org.nathan.jispy;

import org.nathan.interpreter.Jispy;

import java.io.*;

class Main {
    public static void main(String[] args) {
        JispyApp(args);
    }

    static void JispyApp(String[] args) {
        if (args.length == 2) {
            var f = new File(args[0]);
            var l = new File(args[1]);
            if (f.exists() && l.exists()) {
                Jispy interpreter = new Jispy();
                interpreter.loadLib(l);
                interpreter.runFile(f);
            }
        }
        else {
            Jispy interpreter = new Jispy();
            interpreter.repl();
        }
    }
}
