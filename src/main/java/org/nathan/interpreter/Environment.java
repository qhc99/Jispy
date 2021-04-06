package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static org.nathan.interpreter.JispyInterpreter.*;
import static org.nathan.interpreter.NumericOperators.*;
import static org.nathan.interpreter.Utils.isNil;



class Environment extends HashMap<Object, Object> {

    private Environment outer;

    private static final boolean DEBUG = false;

    Environment(@NotNull Object params, @NotNull List<Object> args, Environment outer) {
        this.outer = outer;
        if (params instanceof Symbol) {
            put(params, args);
        }
        else {
            List<Object> p = (List<Object>) params;
            if (p.size() == args.size()) {
                var pi = p.iterator();
                var ai = args.iterator();
                while (pi.hasNext()) {
                    put(pi.next(), ai.next());
                }
            }
            else {
                throw new Exceptions.TypeException(String.format("'expected %s, given %s",
                        evalToString(params),
                        evalToString(args)));
            }
        }
    }

    Environment(@NotNull List<Map.Entry<Object, Object>> entries) {
        for (var e : entries) {
            this.put(e.getKey(), e.getValue());
        }
    }


    Environment find(@NotNull Object o) {
        if (DEBUG) {
            System.out.println(String.format("find symbol: <%s> in %s", o, this.hashCode()));
        }
        if (containsKey(o)) { return this; }
        else if (outer == null) { throw new Exceptions.LookUpException(o.toString()); }
        else { return outer.find(o); }

    }

    @Override
    public String toString() {
        var s = new StringBuilder();
        for (var i : entrySet()) {
            s.append(String.format("%s\n", i.getKey().toString()));
        }
        s.append("\nouter:\n");
        if (outer != null) {
            s.append(outer.toString());
        }
        return s.toString();
    }

    static Environment NewStandardEnv() {
        List<Map.Entry<Object, Object>> m = Arrays.asList(
                Map.entry(new Symbol("+"), (Lambda) args -> {
                    if (args.size() < 1) { throw new Exceptions.ArgumentsCountException(); }
                    if (args.size() == 1) {
                        var val = args.get(0);
                        if (lessThan(val, 0)) {
                            val = negative(val);
                        }
                        return val;
                    }
                    else {
                        var res = args.stream().reduce(NumericOperators::plus);
                        return res.get();
                    }
                }),
                Map.entry(new Symbol("-"), (Lambda) args ->
                {
                    if (args.size() != 1 && args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    if (args.size() == 1) {
                        var val = args.get(0);
                        if (lessThan(0, val)) {
                            val = negative(val);
                        }
                        return val;
                    }
                    else {
                        return minus(args.get(0), args.get(1));
                    }
                }),
                Map.entry(new Symbol("*"), (Lambda) args ->
                {
                    if (args.size() < 2) { throw new Exceptions.ArgumentsCountException(); }
                    return args.stream().reduce(NumericOperators::multiply).get();
                }),
                Map.entry(new Symbol("/"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return divide(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol(">"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return lessThan(args.get(1), args.get(0));
                })),
                Map.entry(new Symbol("<"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return lessThan(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol(">="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return lessOrEqual(args.get(1), args.get(0));
                })),
                Map.entry(new Symbol("<="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return lessOrEqual(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol("="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return equal(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol("abs"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    if (value(args.get(0)) >= 0) {
                        return args.get(0);
                    }
                    else {
                        return negative(args.get(0));
                    }
                })),
                Map.entry(new Symbol("append"), (Lambda) (args ->
                {
                    if (args.size() < 2) { throw new Exceptions.ArgumentsCountException(); }
                    List<Object> res = new ArrayList<>((List<Object>) args.get(0));
                    for (int i = 1; i < args.size(); i++) {
                        res.addAll((List<Object>) args.get(i));
                    }
                    return res;
                })),
                Map.entry(new Symbol("apply"), (Lambda) (args ->
                {
                    Object proc = args.get(0);
                    return ((Lambda) proc).apply(args.subList(1, args.size()));
                })),
                Map.entry(new Symbol("begin"), (Lambda) (args -> args.get(args.size() - 1))),
                Map.entry(new Symbol("car"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return ((List<Object>) args.get(0)).get(0);
                })),
                Map.entry(new Symbol("cdr"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    var t = (List<Object>) (args.get(0));
                    return t.subList(1, t.size());
                })),
                Map.entry(new Symbol("cons"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    var content = (Collection<?>) args.get(1);
                    List<Object> t = new ArrayList<>(content.size()+1);
                    t.add(args.get(0));
                    t.addAll(content);
                    return t;
                })),
                Map.entry(new Symbol("eq?"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) == args.get(1);
                })),
                Map.entry(new Symbol("expt"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return Math.pow(value(args.get(0)), value(args.get(1)));
                })),
                Map.entry(new Symbol("equal?"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry(new Symbol("length"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return ((List<Object>) args.get(0)).size();
                })),
                Map.entry(new Symbol("list"), (Lambda) (args ->
                {
                    if (args.size() < 1) { throw new Exceptions.ArgumentsCountException(); }
                    return new ArrayList<>(args);
                })),
                Map.entry(new Symbol("list?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof List;
                })),
                Map.entry(new Symbol("map"), (Lambda) (args -> {
                    if (args.size() < 1) { throw new Exceptions.ArgumentsCountException(); }
                    Lambda proc = (Lambda) (args.get(0));
                    var lists = args.subList(1, args.size());
                    var res = new ArrayList<>();
                    while (true) {
                        List<Object> vals = new ArrayList<>(lists.size());
                        for (int i = 0; i < lists.size(); i++) {
                            if (!isNil(lists.get(i))) {
                                var list = (List<Object>) (lists.get(i));
                                vals.add(list.get(0));
                                lists.set(i, list.subList(1, list.size()));
                            }
                        }
                        if (vals.size() == lists.size()) {
                            res.add(proc.apply(vals));
                        }
                        else {
                            break;
                        }
                    }
                    return res;

                })),
                Map.entry(new Symbol("max"), (Lambda) (args -> args.stream().max((o1, o2) -> {
                    var t = value(o1) - value(o2);
                    if (t > 0) { return 1; }
                    else if (t < 0) { return -1; }
                    else { return 0; }
                }))),
                Map.entry(new Symbol("min"), (Lambda) (args -> args.stream().min((o1, o2) -> {
                    var t = value(o1) - value(o2);
                    if (t > 0) { return 1; }
                    else if (t < 0) { return -1; }
                    else { return 0; }
                }))),
                Map.entry(new Symbol("not"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return !(boolean) args.get(0);
                })),
                Map.entry(new Symbol("null?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return isNil(args.get(0));
                })),
                Map.entry(new Symbol("number?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof Integer ||
                            args.get(0) instanceof Double ||
                            args.get(0) instanceof Complex;
                })),
                Map.entry(new Symbol("print"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    System.out.println(args.get(0));
                    return null;
                }),
                Map.entry(new Symbol("procedure?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof Procedure;
                })),
                Map.entry(new Symbol("round"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return Math.round(value(args.get(0)));
                })),
                Map.entry(new Symbol("symbol?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof Symbol;
                })),
                Map.entry(new Symbol("pi"), Math.PI),
                Map.entry(new Symbol("nil"), JispyInterpreter.Nil),
                Map.entry(new Symbol("boolean?"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof Boolean;
                }),
                Map.entry(new Symbol("port?"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return args.get(0) instanceof File;
                }),
                Map.entry(new Symbol("call/cc"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    return callcc((Lambda) args.get(0));
                }),
                Map.entry(new Symbol("sqrt"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    var t = args.get(0);
                    if (t instanceof Integer) {
                        int i = (Integer) t;
                        if (i >= 0) {
                            return Math.sqrt(i);
                        }
                        else {
                            Complex c = new Complex(i);
                            return c.sqrt();
                        }
                    }
                    else if (t instanceof Double) {
                        double d = (Double) t;
                        if (d >= 0) {
                            return Math.sqrt(d);
                        }
                        else {
                            Complex c = new Complex(d);
                            return c.sqrt();
                        }
                    }
                    else if (t instanceof Complex) {
                        Complex c = (Complex) t;
                        return c.sqrt();
                    }
                    else { throw new Exceptions.SyntaxException(evalToString(t) + " is not number"); }
                }),

                Map.entry(new Symbol("display"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    System.out.print(evalToString(args.get(0)));
                    return null;
                }),
                Map.entry(new Symbol("port?"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Exceptions.ArgumentsCountException(); }
                    if (args.get(0) instanceof String) {
                        return new File((String) args.get(0)).exists();
                    }
                    else if (args.get(0) instanceof Symbol) {
                        return new File(((Symbol) args.get(0)).str).exists();
                    }
                    else { throw new RuntimeException(); }
                }));
        return new Environment(m);
    }
}
