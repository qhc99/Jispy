package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.nathan.interpreter.literalLexer.FloatingPointLiterals;
import org.nathan.interpreter.magic.MagicUtils;

import java.util.Optional;

class NumericOperators{
    static boolean lessThan(@NotNull Object a, @NotNull Object b){
        return value(a) < value(b);
    }

    static boolean lessOrEqual(@NotNull Object a, @NotNull Object b){
        return value(a) <= value(b);
    }

    static boolean equal(@NotNull Object a, @NotNull Object b){
        if((!(a instanceof Complex)) && (!(b instanceof Complex))){
            return value(a) == value(b);
        }
        else{
            return a.equals(b);
        }
    }

    static @NotNull Object negative(@NotNull Object a){
        if(a instanceof Integer){ return -(Integer) a; }
        else if(a instanceof Double){ return -(Double) a; }
        else if(a instanceof Complex){ return ((Complex) a).negate(); }
        else{ throw new SyntaxException("not number"); }

    }

    static double value(@NotNull Object o){
        if(o instanceof Double){ return (Double) o; }
        else if(o instanceof Integer){ return (Integer) o; }
        else{ throw new SyntaxException("not number"); }
    }

    static @NotNull Object plus(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _plus);
    }

    static @NotNull Object minus(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _minus);
    }

    static @NotNull Object divide(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _divide);
    }

    static @NotNull Object multiply(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _multiply);
    }

    private static Object biDispatch(@NotNull Object a, @NotNull Object b, @NotNull NumericOperators.NumericBiFunc op){
        if(a instanceof Integer){
            if(b instanceof Integer){
                return op.apply((Integer) a,(Integer) b);
            }
            else if(b instanceof Double){
                return op.apply((Integer) a, (Double) b);
            }
            else if(b instanceof Complex){
                return op.apply((Integer) a, (Complex) b);
            }
        }
        else if(a instanceof Double){
            if(b instanceof Integer){
                return op.apply((Double) a, (Integer) b);
            }
            else if(b instanceof Double){
                return op.apply((Double) a, (Double) b);
            }
            else if(b instanceof Complex){
                return op.apply((Double) a, (Complex) b);
            }
        }
        else if(a instanceof Complex){
            if(b instanceof Integer){
                return op.apply((Complex) a, (Integer) b);
            }
            else if(b instanceof Double){
                return op.apply((Complex) a, (Double) b);
            }
            else if(b instanceof Complex){
                return op.apply((Complex) a, (Complex) b);
            }
        }
        throw new RuntimeException("unexpected error");
    }

    private static final NumericBiFunc _minus = new NumericBiFunc(){
        @Override
        public Integer apply(Integer a, Integer b){
            return a - b;
        }

        @Override
        public Double apply(Integer a, Double b){
            return a - b;
        }

        @Override
        public Complex apply(Integer a, Complex b){
            return b.negate().add(a);
        }

        @Override
        public Double apply(Double a, Integer b){
            return a - b;
        }

        @Override
        public Double apply(Double a, Double b){
            return a - b;
        }

        @Override
        public Complex apply(Double a, Complex b){
            return b.negate().add(a);
        }

        @Override
        public Complex apply(Complex a, Integer b){
            return a.negate().add(b).negate();
        }

        @Override
        public Complex apply(Complex a, Double b){
            return a.negate().add(b).negate();
        }

        @Override
        public Complex apply(Complex a, Complex b){
            return a.negate().add(b).negate();
        }
    };

    private static final NumericBiFunc _plus = new SymmetryNumericBiFunc(){
        @Override
        public Integer apply(Integer a, Integer b){
            return a + b;
        }

        @Override
        public Double apply(Integer a, Double b){
            return a + b;
        }

        @Override
        public Complex apply(Integer a, Complex b){
            return b.add(a);
        }

        @Override
        public Double apply(Double a, Double b){
            return a + b;
        }

        @Override
        public Complex apply(Double a, Complex b){
            return b.add(a);
        }

        @Override
        public Complex apply(Complex a, Complex b){
            return a.add(b);
        }
    };

    private static final NumericBiFunc _divide = new NumericBiFunc(){
        @Override
        public Integer apply(Integer a, Integer b){
            return a / b;
        }

        @Override
        public Double apply(Integer a, Double b){
            return a / b;
        }

        @Override
        public Complex apply(Integer a, Complex b){
            return new Complex(1).divide(b.divide(a));
        }

        @Override
        public Double apply(Double a, Integer b){
            return a / b;
        }

        @Override
        public Double apply(Double a, Double b){
            return a / b;
        }

        @Override
        public Complex apply(Double a, Complex b){
            return new Complex(1).divide(b.divide(a));
        }

        @Override
        public Complex apply(Complex a, Integer b){
            return a.divide(b);
        }

        @Override
        public Complex apply(Complex a, Double b){
            return a.divide(b);
        }

        @Override
        public Complex apply(Complex a, Complex b){
            return a.divide(b);
        }
    };

    private static final NumericBiFunc _multiply = new SymmetryNumericBiFunc(){
        @Override
        public Integer apply(Integer a, Integer b){
            return a * b;
        }

        @Override
        public Double apply(Integer a, Double b){
            return a * b;
        }

        @Override
        public Complex apply(Integer a, Complex b){
            return b.multiply(a);
        }

        @Override
        public Double apply(Double a, Double b){
            return a * b;
        }

        @Override
        public Complex apply(Double a, Complex b){
            return b.multiply(a);
        }

        @Override
        public Complex apply(Complex a, Complex b){
            return a.multiply(b);
        }
    };

    private interface NumericBiFunc{
        Integer apply(Integer a, Integer b);

        Double apply(Integer a, Double b);

        Complex apply(Integer a, Complex b);

        Double apply(Double a, Integer b);

        Double apply(Double a, Double b);

        Complex apply(Double a, Complex b);

        Complex apply(Complex a, Integer b);

        Complex apply(Complex a, Double b);

        Complex apply(Complex a, Complex b);
    }

    private interface SymmetryNumericBiFunc extends NumericBiFunc{

        default Double apply(Double a, Integer b){
            return apply(b,a);
        }

        default Complex apply(Complex a, Integer b){
            return apply(b,a);
        }

        default Complex apply(Complex a, Double b){
            return apply(b,a);
        }

    }

    static Optional<Complex> tryParseImaginary(@NotNull String s){
        if(s.length() <= 1){
            return Optional.empty();
        }
        else if(!s.endsWith("i")){
            return Optional.empty();
        }
        else if(!Character.isDigit(s.charAt(s.length() - 2))){
            return Optional.empty();
        }
        else{
            var img = s.substring(0, s.length() - 1);
            var i = MagicUtils.tryParseInt(img);
            if(i.isPresent()){
                return Optional.of(new Complex(0, i.get()));
            }
            else{
                var d = tryParseDouble(img);
                return d.map(aDouble -> new Complex(0, aDouble));
            }
        }
    }

    /**
     * @param s string
     * @return Double
     * @see
     * <a href="https://docs.oracle.com/javase/specs/jls/se15/html/jls-3.html#jls-Digits">java 15 language specification 3.10.2. Floating-Point Literals</a>
     */
    static Optional<Double> tryParseDouble(@NotNull String s){
        if(FloatingPointLiterals.isDoubleParsable(s)){
            return Optional.of(Double.parseDouble(s));
        }
        else{
            return Optional.empty();
        }
    }
}
