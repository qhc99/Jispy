import org.nathan.interpreter.Jispy;

import java.io.File;

class Main {
    public static void main(String[] args) {
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
