import org.apache.commons.math3.complex.Complex;
import org.nathan.interpreter.InputPort;
import org.nathan.interpreter.Jispy;
import org.nathan.interpreter.Symbol;

import java.io.File;

import static org.nathan.interpreter.Jispy.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            var f = new File(args[0]);
            if (f.exists()) {
                runFile(f);
            }
        }
        else {
            System.out.println(evalScripts("(begin (define L (list 1 2 3)) (list `(testing ,@L testing) `(testing ,L testing) ) )"));
        }
    }
}
