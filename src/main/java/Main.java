

import com.google.common.primitives.Doubles;
import org.nathan.interpreter.Jispy;

import java.io.*;

class Main {
    public static void main(String[] args) {
//        JispyApp(args);
        System.out.println(Doubles.tryParse("00__00."));
        var t = +.0;
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
