package org.nathan.interpreter;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.nathan.interpreter.Operators.*;
import static org.nathan.interpreter.Utils.*;


public class StandardEnv {
    static Map<Object, Object> NewEnv() {
        return Map.ofEntries(
                Map.entry("+", (Function<List<Object>, Object>) args -> {
                    if (args.size() < 1) throw new Jisp.ArgumentsCountException();
                    if (args.size() == 1) {
                        var val = args.get(0);
                        if (lessThan(val, 0)) {
                            val = negative(val);
                        }
                        return val;
                    }
                    else {
                        var res = args.stream().reduce(Operators::plus);
                        return res.get();
                    }
                }),
                Map.entry("-", (Function<List<Object>, Object>) args ->
                {
                    if (args.size() != 1 && args.size() != 2) throw new Jisp.ArgumentsCountException();
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
                Map.entry("*", (Function<List<Object>, Object>) args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return multiply(args.get(0), args.get(1));
                }),
                Map.entry("/", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return divide(args.get(0), args.get(1));
                })),
                Map.entry(">", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return lessThan(args.get(1), args.get(0));
                })),
                Map.entry("<", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return lessThan(args.get(0), args.get(1));
                })),
                Map.entry(">=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return lessOrEqual(args.get(1), args.get(0));
                })),
                Map.entry("<=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return lessOrEqual(args.get(0), args.get(1));
                })),
                Map.entry("=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return equal(args.get(0),args.get(1));
                })),
                Map.entry("abs", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    if (value(args.get(0)) >= 0) {
                        return args.get(0);
                    }
                    else {
                        return negative(args.get(0));
                    }
                })),
                Map.entry("append", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() < 2) throw new Jisp.ArgumentsCountException();
                    Jisp.SchemeList t = null;
                    for (int i = args.size() - 1; i >= 0; i--) {
                        if (i == args.size() - 1) {
                            t = new Jisp.SchemeList(args.get(i), null);
                        }
                        else {
                            t = new Jisp.SchemeList(args.get(i), t);
                        }
                    }
                    return null;
                })),
                Map.entry("apply", (Function<List<Object>, Object>) (args ->
                {
                    Object proc = args.get(0);
                    //noinspection unchecked
                    return ((Function<List<Object>, Object>) proc).apply(args.subList(1, args.size()));
                })),
                Map.entry("begin", (Function<List<Object>, Object>) (args -> args.get(args.size() - 1))),
                Map.entry("car", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return ((Jisp.SchemeList) args.get(0)).Car;
                })),
                Map.entry("cdr", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return ((Jisp.SchemeList) (args.get(0))).Cdr;
                })),
                Map.entry("cons", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    if (args.get(0).getClass().equals(Jisp.SchemeList.class)) {
                        var s = (Jisp.SchemeList) args.get(0);
                        s.Cdr = args.get(1);
                        return s;
                    }
                    else {
                        return new Jisp.SchemeList(args.get(0), new Jisp.SchemeList(args.get(1), null));
                    }

                })),
                Map.entry("eq?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return args.get(0) == args.get(1);
                })),
                Map.entry("expt", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return Math.pow(value(args.get(0)), value(args.get(1)));
                })),
                Map.entry("equal?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new Jisp.ArgumentsCountException();
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry("length", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    int len = 1;
                    var ptr = (Jisp.SchemeList) args.get(0);
                    while (!ptr.Cdr.equals("'()")) {
                        ptr = (Jisp.SchemeList) ptr.Cdr;
                        len++;
                    }
                    return len;
                })),
                Map.entry("list", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() < 1) throw new Jisp.ArgumentsCountException();
                    var res = new Jisp.SchemeList();
                    res.Car = args.get(0);
                    if (args.size() == 1) {
                        res.Cdr = "'()";
                    }
                    else {
                        res.Cdr = new Jisp.SchemeList();
                        var ptr = (Jisp.SchemeList) res.Cdr;
                        for (int i = 1; i < args.size(); i++) {
                            ptr.Car = args.get(i);
                        }
                    }

                    return res;
                })),
                Map.entry("list?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return args.get(0).getClass().equals(Jisp.SchemeList.class);
                })),
                Map.entry("map", (Function<List<Object>, Object>) (args -> {
                    throw new RuntimeException("not implemented.");
                })),
                Map.entry("max", (Function<List<Object>, Object>) (args -> {
                    throw new RuntimeException("not implemented");
                })),
                Map.entry("min", (Function<List<Object>, Object>) (args -> {
                    throw new RuntimeException("not implemented");
                })),
                Map.entry("not", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return !(boolean) args.get(0);
                })),
                Map.entry("null?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return args.get(0).equals("'()");
                })),
                Map.entry("number?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    Object type = args.get(0).getClass();
                    return type.equals(Integer.class) || type.equals(Double.class);
                })),
                Map.entry("print", (Function<List<Object>, Object>) args -> {
                    throw new RuntimeException("not implemented.");
                }),
                Map.entry("procedure?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    Object type = args.get(0).getClass();
                    return type.equals(Function.class) || type.equals(Consumer.class);
                })),
                Map.entry("round", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return Math.round(value(args.get(0)));
                })),
                Map.entry("symbol?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new Jisp.ArgumentsCountException();
                    return ((String) args.get(0)).startsWith("'");
                })),
                Map.entry("pi", Math.PI)
        );
    }
}
