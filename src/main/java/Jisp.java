import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
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

    private static class Operators {

        static double value(Object o) {
            if (o instanceof Double) {
                return (Double) o;
            }
            else if (o instanceof Integer) {
                return (Integer) o;
            }
            else {
                throw new ClassCastException();
            }
        }

        static boolean lessThan(Object a, Object b) {
            return value(a) < value(b);
        }

        static boolean lessOrEqual(Object a, Object b) {
            return value(a) <= value(b);
        }

        static boolean equal(Object a, Object b){
            return value(a) == value(b);
        }

        static Object negative(Object a){
            if(a.getClass().equals(Integer.class)){
                return -(Integer) a;
            }
            else if(a.getClass().equals(Double.class)){
                return -(Double)a;
            }
            else {
                throw new ClassCastException();
            }
        }

        static Object plus(Object a, Object b) {
            if (a.getClass().equals(Integer.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Integer c = convert(a);
                    Integer d = convert(b);
                    return c + d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Integer c = convert(a);
                    Double d = convert(b);
                    return c + d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else if (a.getClass().equals(Double.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Double c = convert(a);
                    Integer d = convert(b);
                    return c + d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Double c = convert(a);
                    Double d = convert(b);
                    return c + d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else {
                throw new ClassCastException();
            }
        }

        static Object minus(Object a, Object b) {
            if (a.getClass().equals(Integer.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Integer c = convert(a);
                    Integer d = convert(b);
                    return c - d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Integer c = convert(a);
                    Double d = convert(b);
                    return c - d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else if (a.getClass().equals(Double.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Double c = convert(a);
                    Integer d = convert(b);
                    return c - d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Double c = convert(a);
                    Double d = convert(b);
                    return c - d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else {
                throw new ClassCastException();
            }
        }

        static Object divide(Object a, Object b) {
            if (a.getClass().equals(Integer.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Integer c = convert(a);
                    Integer d = convert(b);
                    return c / d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Integer c = convert(a);
                    Double d = convert(b);
                    return c / d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else if (a.getClass().equals(Double.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Double c = convert(a);
                    Integer d = convert(b);
                    return c / d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Double c = convert(a);
                    Double d = convert(b);
                    return c / d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else {
                throw new ClassCastException();
            }
        }

        static Object multiply(Object a, Object b) {
            if (a.getClass().equals(Integer.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Integer c = convert(a);
                    Integer d = convert(b);
                    return c * d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Integer c = convert(a);
                    Double d = convert(b);
                    return c * d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else if (a.getClass().equals(Double.class)) {
                if (b.getClass().equals(Integer.class)) {
                    Double c = convert(a);
                    Integer d = convert(b);
                    return c * d;
                }
                else if (b.getClass().equals(Double.class)) {
                    Double c = convert(a);
                    Double d = convert(b);
                    return c * d;
                }
                else {
                    throw new ClassCastException();
                }
            }
            else {
                throw new ClassCastException();
            }
        }
    }

    private static class ArgumentsCountException extends RuntimeException{
        public ArgumentsCountException(){
            super();
        }
        public ArgumentsCountException(String s){
            super(s);
        }
    }

    private static class LispParseException extends RuntimeException{
        public LispParseException(){
            super();
        }
        public LispParseException(String s){
            super(s);
        }
    }

    private static Map<Object, Object> StandardEnv() {
        return Map.ofEntries(
                Map.entry("+", (Function<List<Object>, Object>) args -> {
                    if (args.size() < 1) throw new ArgumentsCountException();
                    if (args.size() == 1) {
                        var val = args.get(0);
                        if (Operators.lessThan(val,0)) {
                            val = Operators.negative(val);
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
                    if (args.size() != 1 && args.size() != 2) throw new ArgumentsCountException();
                    if (args.size() == 1) {
                        var val = args.get(0);
                        if (Operators.lessThan(0, val)) {
                            val = Operators.negative(val);
                        }
                        return val;
                    }
                    else {
                        return Operators.minus(args.get(0),args.get(1));
                    }
                }),
                Map.entry("*", (Function<List<Object>, Object>) args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.multiply(args.get(0),args.get(1));
                }),
                Map.entry("/", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.divide(args.get(0),args.get(1));
                })),
                Map.entry(">", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.lessThan(args.get(1),args.get(0));
                })),
                Map.entry("<", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.lessThan(args.get(0),args.get(1));
                })),
                Map.entry(">=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.lessOrEqual(args.get(1),args.get(0));
                })),
                Map.entry("<=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Operators.lessOrEqual(args.get(0),args.get(1));
                })),
                Map.entry("=", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry("abs", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    if(Operators.value(args.get(0)) >= 0){
                        return args.get(0);
                    }
                    else {
                        return Operators.negative(args.get(0));
                    }
                })),
                Map.entry("append", (Consumer<List<Object>>) (args ->
                {
                    if (args.size() < 2) throw new ArgumentsCountException();
                    SchemeList t = null;
                    for (int i = args.size() - 1; i >= 0; i--) {
                        if (i == args.size() - 1) {
                            t = new SchemeList(args.get(i), null);
                        }
                        else {
                            t = new SchemeList(args.get(i), t);
                        }
                    }
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
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return ((SchemeList) args.get(0)).Car;
                })),
                Map.entry("cdr", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return ((SchemeList) (args.get(0))).Cdr;
                })),
                Map.entry("cons", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    if (args.get(0).getClass().equals(SchemeList.class)) {
                        var s = (SchemeList) args.get(0);
                        s.Cdr = args.get(1);
                        return s;
                    }
                    else {
                        return new SchemeList(args.get(0), new SchemeList(args.get(1), null));
                    }

                })),
                Map.entry("eq?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry("expt", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return Math.pow(Operators.value(args.get(0)), Operators.value(args.get(1)));
                })),
                Map.entry("equal?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 2) throw new ArgumentsCountException();
                    return args.get(0).equals(args.get(1));
                })),
                Map.entry("length", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    int len = 1;
                    var ptr = (SchemeList) args.get(0);
                    while (!ptr.Cdr.equals("'()")) {
                        ptr = (SchemeList) ptr.Cdr;
                        len++;
                    }
                    return len;
                })),
                Map.entry("list", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() < 1) throw new ArgumentsCountException();
                    var res = new SchemeList();
                    res.Car = args.get(0);
                    if (args.size() == 1) {
                        res.Cdr = "'()";
                    }
                    else {
                        res.Cdr = new SchemeList();
                        var ptr = (SchemeList) res.Cdr;
                        for (int i = 1; i < args.size(); i++) {
                            ptr.Car = args.get(i);
                        }
                    }

                    return res;
                })),
                Map.entry("list?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return args.get(0).getClass().equals(SchemeList.class);
                })),
                Map.entry("map", (Consumer<List<Object>>) (args -> {
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
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return !(boolean) args.get(0);
                })),
                Map.entry("null?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return args.get(0).equals("'()");
                })),
                Map.entry("number?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    Object type = args.get(0).getClass();
                    return type.equals(Integer.class) || type.equals(Double.class);
                })),
                Map.entry("print", (Consumer<List<Object>>) args -> {
                    throw new RuntimeException("not implemented.");
                }),
                Map.entry("procedure?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    Object type = args.get(0).getClass();
                    return type.equals(Function.class) || type.equals(Consumer.class);
                })),
                Map.entry("round", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return Math.round(Operators.value(args.get(0)));
                })),
                Map.entry("symbol?", (Function<List<Object>, Object>) (args ->
                {
                    if (args.size() != 1) throw new ArgumentsCountException();
                    return ((String) args.get(0)).startsWith("'");
                })),
                Map.entry("pi", Math.PI)
        );
    }

    private static final String Nil = "'()";

    private static final Map<Object, Object> GlobalEnv = new HashMap<>(StandardEnv());

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

    private static Object eval(Object x, Map<Object, Object> env) {
        if (x.getClass().equals(String.class)) {
            return env.get(x);
        }
        else if (!x.getClass().equals(ArrayList.class)) {
            return x;
        }
        List<Object> list = convert(x);

        if (list.get(0) == "if") {
            //noinspection unchecked
            var t = (List<Object>) x;
            Object test = t.get(1);
            Object conseq = t.get(2);
            Object alt = t.get(3);
            Object exp = (Boolean) eval(test, env) ? conseq : alt;
            return eval(exp, env);
        }
        else if (list.get(0).equals("define")) {
            //noinspection unchecked
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

    private static Object convertToAtom(String x) {
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

    private static <T> T convert(Object o) {
        //noinspection unchecked
        return (T) o;
    }


}
