

import com.google.common.primitives.Doubles;
import org.nathan.interpreter.Jispy;

import java.io.*;

class Main {
    public static void main(String[] args) {
//        JispyApp(args);
        var t = -0x1;
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
