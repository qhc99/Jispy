package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.nathan.interpreter.Symbol.*;
import static org.nathan.interpreter.Utils.treeList;

@SuppressWarnings("unused")
public class Jispy {

    private static final boolean TIMER_ON = false;

    static class ArgumentsCountException extends RuntimeException {
        public ArgumentsCountException() {
            super();
        }

        public ArgumentsCountException(String s) {
            super(s);
        }
    }


    static class SyntaxException extends RuntimeException {
        public SyntaxException() {
            super();
        }

        public SyntaxException(String s) {
            super(s);
        }
    }

    private static class RuntimeWarning extends RuntimeException {
        Object returnValue;

        RuntimeWarning() {
        }

        RuntimeWarning(String m) {
            super(m);
        }
    }


    static final List<Object> Nil = new ArrayList<>();

    private static final Env GlobalEnv = Env.NewStandardEnv();

    public static void repl() {
        repl("Jis.py>", new InPort(System.in), new BufferedWriter(new OutputStreamWriter(System.out)));
    }

    static void repl(String prompt, @NotNull Object inPort, Writer out) {
        try {
            System.err.write("Jispy version 2.0\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                if (prompt != null) {
                    System.err.println(prompt);
                }
                var x = parse(inPort);
                if (x.equals(eof)) { return; }
                var val = eval(x);
                if (val != null && out != null) {
                    out.write(toString(val));
                    out.write("\n");
                    out.flush();
                }
            } catch (Exception e) {
                if (e instanceof IOException) {
                    e.printStackTrace(System.err);
                    break;
                }
                else {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    static void load(String fileName) {
        var file = new File(fileName);
        repl(null, new InPort(file), null);
    }

    public static Object runScheme(@NotNull String program) {
        return eval(parse(program));
    }

    static Object parse(@NotNull Object in) {
        if (in instanceof String) { in = new InPort((String) in); }
        return expand(read((InPort) in), true);
    }

    static Object eval(Object x) {
        return eval(x, GlobalEnv);
    }

    static Object eval(Object x, @NotNull Env env) {
        while (true) {
            if (x instanceof Symbol) { return env.find(x).get(x); }
            else if (!(x instanceof List)) { return x; }
            List<Object> l = (List<Object>) x;
            var op = l.get(0);
            if (op.equals(_quote)) { return l.get(1); }
            else if (op.equals(_if)) {
                var test = l.get(1);
                var conseq = l.get(2);
                var alt = l.get(3);
                Boolean t = (Boolean) eval(test, env);
                if (t == null) { throw new SyntaxException("null is not boolean"); }
                if (t) { x = conseq; }
                else { x = alt; }
            }
            else if (op.equals(_set)) {
                var v = l.get(1);
                var exp = l.get(2);
                env.find(v).put(v, eval(exp, env));
                return null;
            }
            else if (op.equals(_define)) {
                var v = l.get(1);
                var exp = l.get(2);
                env.put(v, eval(exp, env));
                return null;
            }
            else if (op.equals(_lambda)) {
                var vars = l.get(1);
                var exp = l.get(2);
                return Procedure.newProcedure((Iterable<Object>) vars, exp, env);
            }
            else if (op.equals(_begin)) {
                for (var exp : l.subList(1, l.size() - 1)) eval(exp, env);
                x = l.get(l.size() - 1);
            }
            else {
                Env finalEnv = env;
                List<Object> exps = new ArrayList<>();
//                exps = l.stream().map(exp -> eval(exp, finalEnv)).collect(Collectors.toList());
                for (var exp : l) {
                    exps.add(eval(exp, env));
                }
                var proc = exps.get(0);
                exps = exps.subList(1, exps.size());
                if (proc instanceof Procedure) {
                    Procedure p = (Procedure) proc;
                    x = p.expression();
                    env = new Env(p.parameters(), exps, p.environment());
                }
                else { return ((Lambda) proc).apply(exps); }
            }
        }
    }

    private static Queue<String> tokenize(@NotNull String program) {
        var t = Arrays.stream(program.replace("(", " ( ").replace(")", " ) ").split(" ")).collect(Collectors.toList());
        t.removeIf(s -> s.equals(""));
        return new LinkedList<>(t);
    }

    private static @NotNull Object toAtom(@NotNull String x) {
        if (x.equals("#t")) { return true; }
        else if (x.equals("#f")) { return false; }
        else if (x.startsWith("\\")) { return x.substring(1, x.length() - 1); }
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
                else { return t1; }

            }
            else { return t; }

        }
    }

    private static @NotNull Object readChar(@NotNull InPort inPort) {
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

    private static @NotNull Object read(@NotNull InPort inPort) {
        var token = inPort.nextToken();
        if (token.equals(eof)) { return eof; }
        else { return readAhead(token, inPort); }

    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static @NotNull Object readAhead(@NotNull Object token, @NotNull InPort inPort) {
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
        else if (token.equals(")")) { throw new SyntaxException("unexpected )"); }
        else if (quotes.containsKey(token)) { return treeList(quotes.get(token), read(inPort)); }
        else if (token.equals(eof)) { throw new SyntaxException("unexpected EOF in list"); }
        else { return toAtom((String) token); }
    }

    private static @NotNull String toString(@NotNull Object x) {
        if (x.equals(true)) { return "#t"; }
        else if (x.equals(false)) { return "#f"; }
        else if (x instanceof Symbol) { return x.toString(); }
        else if (x instanceof String) { return ((String) x).substring(1, ((String) x).length() - 1); }
        else if (x instanceof List) {
            var s = new StringBuilder("(");
            for (var i : (List<Object>) x) {
                s.append(toString(i));
                s.append(" ");
            }
            s.delete(s.length() - 1, s.length());
            s.append(")");
            return s.toString();
        }
        else if (x instanceof Complex) { return ComplexFormat.getInstance().format((Complex) x); }
        else { return x.toString(); }
    }

    static Object expand(@NotNull Object x) {
        return expand(x, false);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static Object expand(@NotNull Object x, boolean topLevel) {
        require(x, x != Nil);
        if (!(x instanceof List)) { return x; }
        List<Object> l = (List<Object>) x;
        var op = l.get(0);
        if (op.equals(_quote)) {
            require(x, l.size() == 2);
            return x;
        }
        else if (op.equals(_if)) {
            if (l.size() == 3) { l.add(null); }
            require(x, l.size() == 4);
            return l.stream().map(Jispy::expand).collect(Collectors.toList());
        }
        else if (op.equals(_set)) {
            require(x, l.size() == 3);
            var v = l.get(1);
            require(x, v instanceof Symbol, "can set! only a symbol");
            return treeList(_set, v, expand(l.get(2)));
        }
        else if (op.equals(_define) || op.equals(_define_macro)) {
            require(x, l.size() >= 3);
            var v = l.get(1);
            var body = l.subList(2, l.size());
            if (v instanceof List && !((List<?>) v).isEmpty()) {
                List<Object> lv = (List<Object>) v;
                var f = lv.get(0);
                var args = lv.subList(1, lv.size());
                var t = treeList(_lambda, args);
                t.addAll(body);
                return expand(treeList(op, f, t));
            }
            else {
                require(x, l.size() == 3);
                require(x, v instanceof Symbol, "can define only a symbol");
                var exp = expand(l.get(2));
                if (op.equals(_define_macro)) {
                    require(x, topLevel, "define-macro only allowed at top level");
                    var proc = eval(exp);
                    require(x, proc instanceof Lambda, "macro must be a procedure");
                    macro_table.put((Symbol) v, (Lambda) proc);
                    return null;
                }
                return treeList(_define, v, exp);
            }
        }
        else if (op.equals(_begin)) {
            if (l.size() == 1) { return null; }
            else { return l.stream().map(i -> expand(i, topLevel)).collect(Collectors.toList()); }
        }
        else if (op.equals(_lambda)) {
            require(x, l.size() >= 3);
            var vars = l.get(1);
            var body = l.subList(2, l.size());
            require(x, (vars instanceof List &&
                    ((List<Object>) vars).stream().allMatch(v -> v instanceof Symbol)));
            Object exp;
            if (body.size() == 1) { exp = body.get(0); }
            else {
                List<Object> t = new ArrayList<>();
                exp = t;
                t.add(_begin);
                t.addAll(body);
            }
            return treeList(_lambda, vars, expand(exp));
        }
        else if (op.equals(_quasi_quote)) {
            require(x, l.size() == 2);
            return expandQuasiQuote(l.get(1));
        }
        else if (op instanceof Symbol && macro_table.containsKey(l.get(0))) {
            return expand(macro_table.get(l.get(0)).apply(l.subList(1, l.size())), topLevel);
        }
        else { return l.stream().map(Jispy::expand).collect(Collectors.toList()); }
    }

    private static @NotNull Object expandQuasiQuote(Object x) {
        if (!isPair(x)) {
            return treeList(_quote, x);
        }
        List<Object> l = (List<Object>) x;
        require(x, !l.get(0).equals(_unquote_splicing), "can't splice here");
        if (l.get(0).equals(_unquote)) {
            require(x, l.size() == 2);
            return l.get(1);
        }
        else if (isPair(l.get(0)) && ((List<?>) l.get(0)).get(0).equals(_unquote_splicing)) {
            require(l.get(0), ((List<?>) l.get(0)).size() == 2);
            return treeList(_append, ((List<?>) l.get(0)).get(1), expandQuasiQuote(l.subList(1, l.size())));
        }
        else { return treeList(_cons, expandQuasiQuote(l.get(0)), expandQuasiQuote(l.subList(1, l.size()))); }
    }

    private static boolean isPair(@NotNull Object x) {
        if (x instanceof List) {
            return !((List<?>) x).isEmpty();
        }
        return false;
    }

    private static void require(@NotNull Object x, boolean predicate) {
        require(x, predicate, "wrong length");
    }

    private static void require(@NotNull Object x, boolean predicate, @NotNull String m) {
        if (!predicate) {
            throw new SyntaxException(x.toString() + m);
        }
    }

    static @NotNull Object let(@NotNull Object... arguments) {
        var args = Arrays.stream(arguments).collect(Collectors.toList());
        List<Object> x = treeList(_let);
        x.add(args);
        require(x, x.size() > 1);
        List<Object> bindings = (List<Object>) args.get(0);
        var body = args.subList(1, args.size());
        require(x, bindings.stream().allMatch(b -> b instanceof List &&
                ((List<?>) b).size() == 2 &&
                ((List<?>) b).get(0) instanceof Symbol));
        var vars = bindings.get(0);
        List<Object> vals = (List<Object>) bindings.get(1);
        var t = treeList(_lambda, treeList(vars));
        t.addAll(body.stream().map(Jispy::expand).collect(Collectors.toList()));
        var r = treeList(t);
        r.addAll(vals.stream().map(Jispy::expand).collect(Collectors.toList()));
        return r;
    }

    static @NotNull Object callcc(@NotNull Lambda proc) {
        var ball = new RuntimeWarning("Sorry, can't continue this continuation any longer.");
        try {
            return proc.apply(treeList((Lambda) objects -> {
                raise(objects, ball);
                return null;
            }));
        } catch (RuntimeWarning w) {
            if (w.equals(ball)) {
                return ball.returnValue;
            }
            else {
                throw w;
            }
        }

    }

    private static void raise(@NotNull Object r, @NotNull RuntimeWarning ball) {
        ball.returnValue = r;
        throw ball;
    }

}
