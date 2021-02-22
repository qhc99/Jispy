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
        return biDispatch(a, b, _plusBiFunc);
    }

    static @NotNull Object minus(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _minusBiFunc);
    }

    static @NotNull Object divide(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _divideBiFunc);
    }

    static @NotNull Object multiply(@NotNull Object a, @NotNull Object b){
        return biDispatch(a, b, _multiplyBiFunc);
    }

    private static Object biDispatch(@NotNull Object a, @NotNull Object b, @NotNull BiFunc op){
        NumberType ta = getNumberType(a);
        NumberType tb = getNumberType(b);

        switch(ta){
            case INT -> {
                switch(tb){
                    case INT -> {
                        return op.apply((Integer) a, (Integer) b);
                    }
                    case DOUBLE -> {
                        return op.apply((Integer) a, (Double) b);
                    }
                    case COMPLEX -> {
                        return op.apply((Integer) a, (Complex) b);
                    }
                    default -> throw new RuntimeException();
                }
            }
            case DOUBLE -> {
                switch(tb){
                    case INT -> {
                        return op.apply((Double) a, (Integer) b);
                    }
                    case DOUBLE -> {
                        return op.apply((Double) a, (Double) b);
                    }
                    case COMPLEX -> {
                        return op.apply((Double) a, (Complex) b);
                    }
                    default -> throw new RuntimeException();
                }
            }
            case COMPLEX -> {
                switch(tb){
                    case INT -> {
                        return op.apply((Complex) a, (Integer) b);
                    }
                    case DOUBLE -> {
                        return op.apply((Complex) a, (Double) b);
                    }
                    case COMPLEX -> {
                        return op.apply((Complex) a, (Complex) b);
                    }
                    default -> throw new RuntimeException();
                }
            }
            default -> throw new RuntimeException();
        }
    }

    private static NumberType getNumberType(Object o){
        if(o instanceof Integer){
            return NumberType.INT;
        }
        else if(o instanceof Double){
            return NumberType.DOUBLE;
        }
        else if(o instanceof Complex){
            return NumberType.COMPLEX;
        }
        else{ throw new RuntimeException(String.format("not such value type %s", o.getClass().getName())); }
    }

    private static final MinusBiFunc _minusBiFunc = new MinusBiFunc();

    private static final PlusBiFunc _plusBiFunc = new PlusBiFunc();

    private static final DivideBiFunc _divideBiFunc = new DivideBiFunc();

    private static final MultiplyBiFunc _multiplyBiFunc = new MultiplyBiFunc();

    private interface BiFunc{
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

    private interface SymmetryBiFunc extends BiFunc{

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

    private static class PlusBiFunc implements SymmetryBiFunc{

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
    }

    private static class MinusBiFunc implements BiFunc{

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
    }

    private static class DivideBiFunc implements BiFunc{

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
    }

    private static class MultiplyBiFunc implements SymmetryBiFunc{

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
    }

    private enum NumberType{
        INT, DOUBLE, COMPLEX
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
