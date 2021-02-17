import org.apache.commons.math3.complex.ComplexFormat;
import org.nathan.interpreter.InputPort;
import org.nathan.interpreter.Jispy;
import org.nathan.interpreter.Symbol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static org.nathan.interpreter.Jispy.toAtom;


class Main {
    public static void main(String[] args) {
        JispyApp(args);
    }

    static void JispyApp(String[] args) {
        if (args.length == 1) {
            var f = new File(args[0]);
            if (f.exists()) {
                Jispy.runFile(f);
            }
        }
        else {
            Jispy.repl();
        }
    }

}
