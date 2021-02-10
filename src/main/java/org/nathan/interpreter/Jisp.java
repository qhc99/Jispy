package org.nathan.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.nathan.interpreter.Utils.*;

// TODO transplant from C# code
public class Jisp {
    static class SchemeList implements Iterable<Object> {
        Object Car;
        Object Cdr;

        SchemeList() {
        }

        SchemeList(Object car, Object cdr) {
            Car = car;
            Cdr = cdr;
        }

        private class ThisIterator implements Iterator<Object> {

            @Override
            public boolean hasNext() {
                return !Cdr.equals(Nil);
            }

            @Override
            public Object next() {
                return Cdr;
            }
        }

        @Override
        public Iterator<Object> iterator() {
            return new ThisIterator();
        }
    }

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


    private static final String Nil = "'()";

    private static final Map<Object, Object> GlobalEnv = new HashMap<>(StandardEnv.NewEnv());

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
                val = runScheme(s);
            } catch (Exception e) {
                System.out.printf("%s: %s\n%n", e.getClass().getName(), e.getMessage());
                e.printStackTrace();
            }
            if (val != null) {
                System.out.println(val);
            }
        }
    }

    public static Object runScheme(String program) {
        return eval(parse(program));
    }

    private static Object parse(String program) {
        return readFromTokens(tokenize(program));
    }

    private static Object eval(Object x) {
        return eval(x, GlobalEnv);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static Object eval(Object x, Map<Object, Object> env) {
        if (x.getClass().equals(String.class)) {
            return env.get(x);
        }
        else if (!x.getClass().equals(ArrayList.class)) {
            return x;
        }
        List<Object> list = convert(x);

        if (list.get(0) == "if") {
            var t = (List<Object>) x;
            Object test = t.get(1);
            Object conseq = t.get(2);
            Object alt = t.get(3);
            Object exp = (Boolean) eval(test, env) ? conseq : alt;
            return eval(exp, env);
        }
        else if (list.get(0).equals("define")) {

            var t = (List<Object>) x;
            Object symbol = t.get(1);
            Object exp = t.get(2);
            env.put(symbol, eval(exp, env));
            return null;
        }
        else {
            Function<List<Object>, Object> proc = convert(eval(list.get(0), env));
            List<Object> args = new ArrayList<>();

            for (int i = 1; i < ((List<?>) x).size(); i++) {
                args.add(eval(((List<?>) x).get(i), env));
            }

            return proc.apply(args);
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
        else return convertToAtom(token);
    }

    private static Queue<String> tokenize(String program) {
        var t = Arrays.stream(program.replace("(", " ( ").replace(")", " ) ").split(" ")).collect(Collectors.toList());
        t.removeIf(s -> s.equals(""));
        return new LinkedList<>(t);
    }


}
