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

    static double value(@NotNull Object o){
        if(o instanceof Double){ return (Double) o; }
        else if(o instanceof Integer){ return (Integer) o; }
        else{ throw new SyntaxException("not number"); }
    }

    private static Object biDispatch(@NotNull Object a, @NotNull Object b, @NotNull BiFunc op){
        NumberType ta, tb;
        if(a instanceof Integer){
            ta = NumberType.INT;
        }
        else if(a instanceof Double){
            ta = NumberType.DOUBLE;
        }
        else if(a instanceof Complex){
            ta = NumberType.COMPLEX;
        }
        else{
            throw new RuntimeException(String.format("not such value type %s", a.getClass().getName()));
        }

        if(b instanceof Integer){
            tb = NumberType.INT;
        }
        else if(b instanceof Double){
            tb = NumberType.DOUBLE;
        }
        else if(b instanceof Complex){
            tb = NumberType.COMPLEX;
        }
        else{ throw new RuntimeException(String.format("not such value type %s", b.getClass().getName())); }

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

    private static final MinusBiFunc _minusBiFunc = new MinusBiFunc();

    private static final PlusBiFunc _plusBiFunc = new PlusBiFunc();

    private static final DivideBiFunc _divideBiFunc = new DivideBiFunc();

    private static final MultiplyBiFunc _multiplyBiFunc = new MultiplyBiFunc();

    private static abstract class BiFunc{
        abstract Integer apply(Integer a, Integer b);

        abstract Double apply(Integer a, Double b);

        abstract Complex apply(Integer a, Complex b);

        abstract Double apply(Double a, Integer b);

        abstract Double apply(Double a, Double b);

        abstract Complex apply(Double a, Complex b);

        abstract Complex apply(Complex a, Integer b);

        abstract Complex apply(Complex a, Double b);

        abstract Complex apply(Complex a, Complex b);
    }

    private static class PlusBiFunc extends BiFunc{

        @Override
        Integer apply(Integer a, Integer b){
            return a + b;
        }

        @Override
        Double apply(Integer a, Double b){
            return a + b;
        }

        @Override
        Complex apply(Integer a, Complex b){
            return b.add(a);
        }

        @Override
        Double apply(Double a, Integer b){
            return a + b;
        }

        @Override
        Double apply(Double a, Double b){
            return a + b;
        }

        @Override
        Complex apply(Double a, Complex b){
            return b.add(a);
        }

        @Override
        Complex apply(Complex a, Integer b){
            return a.add(b);
        }

        @Override
        Complex apply(Complex a, Double b){
            return a.add(b);
        }

        @Override
        Complex apply(Complex a, Complex b){
            return a.add(b);
        }
    }

    private static class MinusBiFunc extends BiFunc{

        @Override
        Integer apply(Integer a, Integer b){
            return a - b;
        }

        @Override
        Double apply(Integer a, Double b){
            return a - b;
        }

        @Override
        Complex apply(Integer a, Complex b){
            return b.negate().add(a);
        }

        @Override
        Double apply(Double a, Integer b){
            return a - b;
        }

        @Override
        Double apply(Double a, Double b){
            return a - b;
        }

        @Override
        Complex apply(Double a, Complex b){
            return b.negate().add(a);
        }

        @Override
        Complex apply(Complex a, Integer b){
            return a.negate().add(b).negate();
        }

        @Override
        Complex apply(Complex a, Double b){
            return a.negate().add(b).negate();
        }

        @Override
        Complex apply(Complex a, Complex b){
            return a.negate().add(b).negate();
        }
    }

    private static class DivideBiFunc extends BiFunc{

        @Override
        Integer apply(Integer a, Integer b){
            return a / b;
        }

        @Override
        Double apply(Integer a, Double b){
            return a / b;
        }

        @Override
        Complex apply(Integer a, Complex b){
            return new Complex(1).divide(b.divide(a));
        }

        @Override
        Double apply(Double a, Integer b){
            return a / b;
        }

        @Override
        Double apply(Double a, Double b){
            return a / b;
        }

        @Override
        Complex apply(Double a, Complex b){
            return new Complex(1).divide(b.divide(a));
        }

        @Override
        Complex apply(Complex a, Integer b){
            return a.divide(b);
        }

        @Override
        Complex apply(Complex a, Double b){
            return a.divide(b);
        }

        @Override
        Complex apply(Complex a, Complex b){
            return a.divide(b);
        }
    }

    private static class MultiplyBiFunc extends BiFunc{

        @Override
        Integer apply(Integer a, Integer b){
            return a * b;
        }

        @Override
        Double apply(Integer a, Double b){
            return a * b;
        }

        @Override
        Complex apply(Integer a, Complex b){
            return b.multiply(a);
        }

        @Override
        Double apply(Double a, Integer b){
            return a * b;
        }

        @Override
        Double apply(Double a, Double b){
            return a * b;
        }

        @Override
        Complex apply(Double a, Complex b){
            return b.multiply(a);
        }

        @Override
        Complex apply(Complex a, Integer b){
            return a.multiply(b);
        }

        @Override
        Complex apply(Complex a, Double b){
            return a.multiply(b);
        }

        @Override
        Complex apply(Complex a, Complex b){
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
        if(FloatingPointLiterals.isFloatingPointLiteral(s)){
            return Optional.of(Double.parseDouble(s));
        }
        else{
            return Optional.empty();
        }
    }
}
