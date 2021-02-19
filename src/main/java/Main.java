
import com.google.common.primitives.Doubles;
import org.nathan.interpreter.Jispy;

import java.io.*;
import java.util.Arrays;
import java.util.List;

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
