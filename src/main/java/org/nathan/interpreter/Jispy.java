package org.nathan.interpreter;

import org.nathan.interpreter.Env.Lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


public class Jispy {

    private static final boolean TIMER_ON = false;

    @SuppressWarnings("unused")
    static class ArgumentsCountException extends RuntimeException {
        public ArgumentsCountException() {
            super();
        }

        public ArgumentsCountException(String s) {
            super(s);
        }
    }

    @SuppressWarnings("unused")
    static class LispParseException extends RuntimeException {
        public LispParseException() {
            super();
        }

        public LispParseException(String s) {
            super(s);
        }
    }


    static final String Nil = "'()";

    static final Env GlobalEnv = Env.NewStandardEnv();

    public static void repl() {
        var prompt = "Jis.py>";
        var reader = new BufferedReader(new InputStreamReader(System.in));
        //noinspection InfiniteLoopStatement
        while (true) {
            String s;
            System.out.print(prompt);
            try {
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (s.equals("")) {
                continue;
            }
            Object val = null;
            try {
                long t1, t2;
                if (TIMER_ON){
                    t1 = System.nanoTime();
                }
                val = runScheme(s);
                if (TIMER_ON) {
                    t2 = System.nanoTime();
                    System.out.println((t2 - t1) / Math.pow(10, 6));
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            if (val != null) {
                System.out.println(val);
            }
        }
    }

    public static Object runScheme(String program) {
        return eval(parse(program));
    }

    static Object parse(String program) {
        return readFromTokens(tokenize(program));
    }

    private static Object eval(Object x) {
        return eval(x, GlobalEnv);
    }

    private static Object eval(Object x, Env env) {
        if (x.getClass().equals(String.class)) {
            return env.find(x).get(x);
        }
        else if (!x.getClass().equals(ArrayList.class)) {
            return x;
        }
        List<Object> list = (List<Object>)(x);
        String op = (String)(list.get(0));
        var args = list.subList(1, list.size());
        switch (op) {
            case "if" -> {
                Boolean test = (Boolean)(eval(args.get(0), env));
                var conseq = args.get(1);
                var alt = args.get(2);
                if (test == null) {
                    throw new NullPointerException();
                }
                var exp = test ? conseq : alt;
                return eval(exp, env);
            }
            case "define" -> {
                var symbol = args.get(0);
                var exp = args.get(1);
                env.put(symbol, eval(exp, env));
                return null;
            }
            case "lambda" -> {
                var parameter = args.get(0);
                var body = args.get(1);
                return (Lambda) arguments -> eval(body, new Env((Iterable<Object>) (parameter), arguments, env));
            }
            default -> {
                Lambda proc = (Lambda) (eval(op, env));
                var vals = args.stream().map(arg -> eval(arg, env)).collect(Collectors.toList());
                if (proc == null) {
                    throw new ClassCastException("null is not function");
                }
                return proc.apply(vals);
            }
        }
    }

    private static Object readFromTokens(Queue<String> tokens) {
        if (tokens.size() == 0) throw new LispParseException("unexpected EOF");
        var token = tokens.poll();
        if (token.equals("(")) {
            var l = new ArrayList<>();
            //noinspection ConstantConditions
            while (!tokens.peek().equals(")")) {
                l.add(readFromTokens(tokens));
            }

            tokens.poll();
            return l;
        }
        else if (token.equals(")")) throw new LispParseException("unexpected ')'");
        else return toAtom(token);
    }

    private static Queue<String> tokenize(String program) {
        var t = Arrays.stream(program.replace("(", " ( ").replace(")", " ) ").split(" ")).collect(Collectors.toList());
        t.removeIf(s -> s.equals(""));
        return new LinkedList<>(t);
    }

    private static Object toAtom(String x) {
        boolean succ = false;
        int t = 0;
        try {
            t = Integer.parseInt(x);
            succ = true;
        } catch (NumberFormatException ignore) {

        }
        if (!succ) {
            boolean succ1 = false;
            double t1 = 0;
            try {
                t1 = Double.parseDouble(x);
                succ1 = true;
            } catch (NumberFormatException ignore) {
            }
            if (!succ1) {
                return x;
            }
            else {
                return t1;
            }
        }
        else {
            return t;
        }
    }
}
