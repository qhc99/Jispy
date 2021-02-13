package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import static org.nathan.interpreter.Jispy.*;
import static org.nathan.interpreter.NumericOperators.*;
import static org.nathan.interpreter.Utils.isNil;


class Env extends HashMap<Object, Object> {

    private Env outer;


    Env(@NotNull Iterable<Object> keys, @NotNull Iterable<Object> vals, Env outer) {
        this.outer = outer;
        var keyIter = keys.iterator();
        var valIter = vals.iterator();
        while (keyIter.hasNext() && valIter.hasNext()) {
            put(keyIter.next(), valIter.next());
        }
        if (keyIter.hasNext() || valIter.hasNext()) {
            throw new RuntimeException("env build error");
        }
    }

    Env(@NotNull Iterable<Entry<Object, Object>> entries) {
        for (var e : entries) {
            this.put(e.getKey(), e.getValue());
        }
    }


    Env find(@NotNull Object o) {
        if (containsKey(o)) { return this; }
        else if (outer == null) { throw new RuntimeException("look up error"); }
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

    static Env NewStandardEnv() {
        Map<Object, Object> m = Map.ofEntries(
                Map.entry(new Symbol("+"), (Lambda) args -> {
                    if (args.size() < 1) { throw new Jispy.ArgumentsCountException(); }
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
                    if (args.size() != 1 && args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
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
                    if (args.size() < 2) { throw new Jispy.ArgumentsCountException(); }
                    return args.stream().reduce(NumericOperators::multiply).get();
                }),
                Map.entry(new Symbol("/"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return divide(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol(">"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return lessThan(args.get(1), args.get(0));
                })),
                Map.entry(new Symbol("<"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return lessThan(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol(">="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return lessOrEqual(args.get(1), args.get(0));
                })),
                Map.entry(new Symbol("<="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return lessOrEqual(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol("="), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return equal(args.get(0), args.get(1));
                })),
                Map.entry(new Symbol("abs"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    if (value(args.get(0)) >= 0) {
                        return args.get(0);
                    }
                    else {
                        return negative(args.get(0));
                    }
                })),
                Map.entry(new Symbol("append"), (Lambda) (args ->
                {
                    if (args.size() < 2) { throw new Jispy.ArgumentsCountException(); }
                    List<Object> res = (List<Object>) args.get(0);
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
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return ((List<Object>) args.get(0)).get(0);
                })),
                Map.entry(new Symbol("cdr"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    var t = (List<Object>) (args.get(0));
                    return t.subList(1, t.size());
                })),
                Map.entry(new Symbol("cons"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    List<Object> t = new ArrayList<>();
                    t.add(args.get(0));
                    t.addAll((Collection<?>) args.get(1));
                    return t;
                })),
                Map.entry(new Symbol("eq?"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0) == args.get(1);
                })),
                Map.entry(new Symbol("expt"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return Math.pow(value(args.get(0)), value(args.get(1)));
                })),
                Map.entry(new Symbol("equal?"), (Lambda) (args ->
                {
                    if (args.size() != 2) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry(new Symbol("length"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return ((List<Object>) args.get(0)).size();
                })),
                Map.entry(new Symbol("list"), (Lambda) (args ->
                {
                    if (args.size() < 1) { throw new Jispy.ArgumentsCountException(); }
                    var res = new ArrayList<>();
                    res.add(args.get(0));
                    for (int i = 1; i < args.size(); i++) {
                        res.add(args.get(i));
                    }
                    return res;
                })),
                Map.entry(new Symbol("list?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0) instanceof List;
                })),
                Map.entry(new Symbol("map"), (Lambda) (args -> {
                    if (args.size() < 1) { throw new Jispy.ArgumentsCountException(); }
                    Lambda proc = (Lambda) (args.get(0));
                    var lists = args.subList(1, args.size());
                    var res = new ArrayList<>();
                    while (true) {
                        List<Object> vals = new ArrayList<>();
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
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return !(boolean) args.get(0);
                })),
                Map.entry(new Symbol("null?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return isNil(args.get(0));
                })),
                Map.entry(new Symbol("number?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    Object type = args.get(0).getClass();
                    return type.equals(Integer.class) || type.equals(Double.class);
                })),
                Map.entry(new Symbol("print"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    System.out.println(args.get(0));
                    return null;
                }),
                Map.entry(new Symbol("procedure?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    Object type = args.get(0).getClass();
                    return type.equals(Function.class);
                })),
                Map.entry(new Symbol("round"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return Math.round(value(args.get(0)));
                })),
                Map.entry(new Symbol("symbol?"), (Lambda) (args ->
                {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0) instanceof Symbol;
                })),
                Map.entry(new Symbol("pi"), Math.PI),
                Map.entry(new Symbol("nil"), Jispy.Nil),
                Map.entry(new Symbol("boolean?"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0) instanceof Boolean;
                }),
                Map.entry(new Symbol("port?"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return args.get(0) instanceof File;
                }),
                Map.entry(new Symbol("eval"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return eval(expand(args.get(0)));
                }),
                Map.entry(new Symbol("load"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    load((String) args.get(0));
                    return null;
                }),
                Map.entry(new Symbol("call/cc"), (Lambda) args -> {
                    if (args.size() != 1) { throw new Jispy.ArgumentsCountException(); }
                    return callcc((Lambda) args.get(0));
                }));
        return new Env(m.entrySet());
    }
}
