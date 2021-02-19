package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.jetbrains.annotations.NotNull;
import org.nathan.interpreter.magic.MagicUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.nathan.interpreter.Symbol.*;
import static org.nathan.interpreter.NumericOperators.*;
import static org.nathan.interpreter.Utils.*;

public class Jispy {
    static final List<Object> Nil = Collections.emptyList();
    static final Environment GlobalEnv = Environment.NewStandardEnv();

    @SuppressWarnings({"InfiniteLoopStatement", "unused"})
    public static void repl() {
        String prompt = "Jis.py>";
        InputPort inPort = new InputPort(System.in);
        System.out.println("Jispy version 2.0");
        while (true) {
            try {
                System.out.print(prompt);
                var x = parse(inPort);
                if (x == null) { continue; }
                else if (x.equals(eof)) { continue; }
                evalAndPrint(x);
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    @SuppressWarnings("unused")
    public static void runFile(File file) {
        try (var inPort = new InputPort(file)) {
            while (true) {
                try {
                    var x = parse(inPort);
                    if (x == null) { continue; }
                    else if (x.equals(eof)) { return; }
                    evalAndPrint(x);
                }
                catch (Exception e) {
                    System.out.println(String.format("%s", e.toString()));
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object evalScripts(@NotNull String program) {
        return eval(parse(program), GlobalEnv);
    }

    private static void evalAndPrint(Object x) {
        var val = eval(x, GlobalEnv);
        if (val != null) {
            System.out.println(evalToString(val));
        }
    }

    static void loadLib(String fileName, Environment env) {
        var file = new File(fileName);
        try (var inPort = new InputPort(file)) {
            while (true) {
                try {
                    var x = parse(inPort);
                    if (x == null) { continue; }
                    else if (x.equals(eof)) { return; }
                    eval(x, env);
                }
                catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static Object eval(Object x, @NotNull Environment env) {
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
                boolean testBool = isTrue(eval(test, env));
                if (testBool) { x = conseq; }
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
                return Procedure.newProcedure(vars, exp, env);
            }
            else if (op.equals(_begin)) {
                for (var exp : l.subList(1, l.size() - 1)) eval(exp, env);
                x = l.get(l.size() - 1);
            }
            else {
                Environment finalEnv = env;
                List<Object> exps = l.stream().map(exp -> eval(exp, finalEnv)).collect(Collectors.toList());
                var proc = exps.get(0);
                exps = exps.subList(1, exps.size());
                if (proc instanceof Procedure) {
                    Procedure p = (Procedure) proc;
                    x = p.expression();
                    env = new Environment(p.parameters(), exps, p.environment());
                }
                else { return ((Lambda) proc).apply(exps); }
            }
        }
    }

    static Object parse(@NotNull Object in) {
        if (in instanceof String) {
            var t = read(new InputPort((String) in));
            return expand(t, true);
        }
        else if (in instanceof InputPort) {
            var t = read((InputPort) in);
            return expand(t, true);
        }
        else { throw new RuntimeException(); }
    }

    private static @NotNull Object read(@NotNull InputPort inPort) {
        var token = inPort.nextToken();
        if (token.equals(eof)) { return eof; }
        else { return readAhead(token, inPort); }

    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static @NotNull Object readAhead(@NotNull Object token, @NotNull InputPort inPort) {
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
        else {
            return toAtom((String) token);
        }
    }

    private static @NotNull Object toAtom(@NotNull String x) {
        if (x.equals("#t")) { return true; }
        else if (x.equals("#f")) { return false; }
        else if (x.startsWith("\\")) { return x.substring(1, x.length() - 1); }
        else if (stringContainsDigit(x)) {
            var isInt = MagicUtils.tryParseInt(x);
            if (isInt.isPresent()) {
                return isInt.get();
            }
            else {
                var isDouble = tryParseDouble(x);
                if (isDouble.isPresent()) {
                    return isDouble.get();
                }
                else {
                    var isComplex = NumericOperators.tryParseImaginary(x);
                    if (isComplex.isPresent()) {
                        return isComplex.get();
                    }
                    else {
                        return new Symbol(x);
                    }
                }
            }
        }
        else {
            var isComplex = NumericOperators.tryParseImaginary(x);
            if (isComplex.isPresent()) {
                return isComplex.get();
            }
            else {
                return new Symbol(x);
            }
        }
    }

    static String evalToString(Object x) {
        if (x == null) { return null; }
        else if (x.equals(true)) { return "#t"; }
        else if (x.equals(false)) { return "#f"; }
        else if (x instanceof Symbol) { return x.toString(); }
        else if (x instanceof String) { return ((String) x).substring(1, ((String) x).length() - 1); }
        else if (x instanceof List) {
            var s = new StringBuilder("(");
            for (var i : (List<Object>) x) {
                s.append(evalToString(i));
                s.append(" ");
            }
            if (((List<?>) x).size() >= 1) {
                s.delete(s.length() - 1, s.length());
            }
            s.append(")");
            return s.toString();
        }
        else if (x instanceof Complex) { return ComplexFormat.getInstance().format((Complex) x); }
        else { return x.toString(); }
    }

    static Object expand(Object x) {
        return expand(x, false);
    }

    private static Object expand(Object x, boolean topLevel) {
        require(x, !isNil(x));
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
                    var proc = eval(exp, GlobalEnv);
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
                    ((List<Object>) vars).stream().allMatch(v -> v instanceof Symbol)) ||
                    vars instanceof Symbol, "illegal lambda argument list");
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
        else if (op instanceof Symbol && macro_table.containsKey(op)) {
            return expand(macro_table.get(op).apply(l.subList(1, l.size())), topLevel);
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
        return !isNil(x) && x instanceof List;
    }

    private static void require(Object x, boolean predicate) {
        require(x, predicate, " wrong length");
    }

    private static void require(Object x, boolean predicate, @NotNull String m) {
        if (!predicate) {
            throw new SyntaxException(evalToString(x) + m);
        }
    }

    static @NotNull Object let(@NotNull List<Object> args) {
        List<Object> x = treeList(_let);
        x.add(args);
        require(x, x.size() > 1);
        List<List<Object>> bindings;
        try {
            bindings = (List<List<Object>>) args.get(0);
        }
        catch (ClassCastException e) {
            throw new ClassCastException("illegal binding list");
        }
        List<Object> body = args.subList(1, args.size());
        require(x, bindings.stream().allMatch(b -> b != null &&
                b.size() == 2 &&
                b.get(0) instanceof Symbol), "illegal binding list");
        List<Object> vars = bindings.stream().map(l -> l.get(0)).collect(Collectors.toList());
        List<Object> vals = bindings.stream().map(l -> l.get(1)).collect(Collectors.toList());
        var t = treeList(_lambda, vars);
        t.addAll(body.stream().map(Jispy::expand).collect(Collectors.toList()));
        var r = treeList(t);
        r.addAll(vals.stream().map(Jispy::expand).collect(Collectors.toList()));
        return r;
    }

    static @NotNull Object callcc(@NotNull Lambda proc) {
        var ball = new RuntimeWarning("Sorry, can't continue this continuation any longer.");
        try {
            return proc.apply(treeList((Lambda) objects -> {
                raise(objects.get(0), ball);
                return null;
            }));
        }
        catch (RuntimeWarning w) {
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

    /*
    private static @NotNull Object readChar(@NotNull InputPort inPort) {
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
    */


}
