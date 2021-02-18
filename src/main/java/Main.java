
import org.nathan.interpreter.Jispy;

import java.io.*;
import java.util.*;



class Main {
    public static void main(String[] args) {
        JispyApp(args);
//        benchmark();
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

    static void benchmark(){
        List<Integer> l  = new ArrayList<>();
        for(int i = 0; i < 10_000_000; i++){
            l.add(1000);
        }
        long res = 0, time=0;
        long t1,t2;
        for(int i = 0; i < 10_000_000-1;i++){
            t1 = System.nanoTime();
            res += l.get(i);
            t2 = System.nanoTime();
            time += (t2-t1);
        }
        System.out.printf("%fms\n", time/Math.pow(10,6));
        System.out.println(res);
    }
}
