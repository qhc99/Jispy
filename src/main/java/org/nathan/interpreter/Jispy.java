package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;
import org.nathan.interpreter.Env.Lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.nathan.interpreter.Symbol.eof;
import static org.nathan.interpreter.Symbol.quotes;


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
    static class SyntaxException extends RuntimeException {
        public SyntaxException() {
            super();
        }

        public SyntaxException(String s) {
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
                if (TIMER_ON) t1 = System.nanoTime();
                val = runScheme(s);
                if (TIMER_ON) {
                    t2 = System.nanoTime();
                    System.out.println((t2 - t1) / Math.pow(10, 6));
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            if (val != null) System.out.println(val);
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
        if (x.getClass().equals(String.class)) return env.find(x).get(x);
        else if (!x.getClass().equals(ArrayList.class)) return x;
        List<Object> list = (List<Object>) (x);
        String op = (String) (list.get(0));
        var args = list.subList(1, list.size());
        switch (op) {
            case "if" -> {
                Boolean test = (Boolean) (eval(args.get(0), env));
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
        if (tokens.size() == 0) throw new SyntaxException("unexpected EOF");
        var token = tokens.poll();
        if (token.equals("(")) {
            var l = new ArrayList<>();
            //noinspection ConstantConditions
            while (!tokens.peek().equals(")")) l.add(readFromTokens(tokens));
            tokens.poll();
            return l;
        }
        else if (token.equals(")")) throw new SyntaxException("unexpected ')'");
        else return toAtom(token);
    }

    private static Queue<String> tokenize(String program) {
        var t = Arrays.stream(program.replace("(", " ( ").replace(")", " ) ").split(" ")).collect(Collectors.toList());
        t.removeIf(s -> s.equals(""));
        return new LinkedList<>(t);
    }

    private static Object toAtom(String x) {
        if (x.equals("#t")) return true;
        else if (x.equals("#f")) return false;
        else if (x.startsWith("\\")) return x.substring(1, x.length() - 1);
        else {
            boolean isInt = false;
            int t = 0;
            try {
                t = Integer.parseInt(x);
                isInt = true;
            } catch (NumberFormatException ignore) {

            }
            if (!isInt) {
                boolean isDouble = false;
                double t1 = 0;
                try {
                    t1 = Double.parseDouble(x);
                    isDouble = true;
                } catch (NumberFormatException ignore) {
                }
                if (!isDouble) {
                    try {
                        return ComplexFormat.getInstance().parse(x);
                    } catch (MathParseException ignore) {
                        return new Symbol(x);
                    }
                }
                else return t1;

            }
            else return t;

        }
    }

    private static Object readChar(InPort inPort) {
        if (!inPort.line.equals("")) {
            var c = inPort.line.charAt(0);
            inPort.line = inPort.line.substring(1);
            return c;
        }
        else {
            String c;
            try {
                c = Character.toString(inPort.file.read());
            } catch (IOException ignore) {
                return eof;
            }
            return c;
        }
    }

    private static Object read(InPort inPort) {
        var token = inPort.nextToken();
        if (token == eof) return eof;
        else return readAhead(token, inPort);

    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static Object readAhead(Object token, InPort inPort) {
        if (token.equals("(")) {
            List<Object> l = new ArrayList<>();
            while (true) {
                token = inPort.nextToken();
                if (token.equals(")")) {
                    return l;
                }
                else {
                    l.add(readAhead(token, inPort));
                }
            }
        }
        else if (token.equals(")")) throw new SyntaxException("unexpected )");
        else if (quotes.containsKey(token)) return Arrays.asList(quotes.get(token), read(inPort));
        else if (token == eof) throw new SyntaxException("unexpected EOF in list");
        else return toAtom((String) token);
    }

    private static String toString(Object x) {
        if (x.equals(true)) return "#t";
        else if (x.equals(false)) return "#f";
        else if (x instanceof Symbol) return x.toString();
        else if (x instanceof String) return ((String) x).substring(1, ((String) x).length() - 1);
        else if (x instanceof ArrayList) {
            var s = new StringBuilder("(");
            for(var i : (ArrayList<Object>)x){
                s.append(toString(i));
                s.append(" ");
            }
            s.delete(s.length()-1,s.length());
            s.append(")");
            return s.toString();
        }
        else if(x instanceof Complex) return ComplexFormat.getInstance().format((Complex) x);
        else return x.toString();
    }
}
