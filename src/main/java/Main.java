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
            repl();
        }
    }
}
