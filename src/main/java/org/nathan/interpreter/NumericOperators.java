package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.nathan.interpreter.LiteralParser.FloatingPointLiterals;
import org.nathan.interpreter.magic.MagicUtils;

import java.util.Optional;

class NumericOperators {
    static boolean lessThan(@NotNull Object a, @NotNull Object b) {
        return value(a) < value(b);
    }

    static boolean lessOrEqual(@NotNull Object a, @NotNull Object b) {
        return value(a) <= value(b);
    }

    static boolean equal(@NotNull Object a, @NotNull Object b) {
        if ((!(a instanceof Complex)) && (!(b instanceof Complex))) {
            return value(a) == value(b);
        }
        else {
            return a.equals(b);
        }
    }

    static @NotNull Object negative(@NotNull Object a) {
        if (a instanceof Integer) { return -(Integer) a; }
        else if (a instanceof Double) { return -(Double) a; }
        else if (a instanceof Complex) { return ((Complex) a).negate(); }
        else { throw new SyntaxException("not number"); }

    }

    static @NotNull Object plus(@NotNull Object a, @NotNull Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c + d;
            }
            else if (b instanceof Double) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c + d;
            }
            else if (b instanceof Complex) {
                Integer c = (Integer) (a);
                return ((Complex) b).add(c);
            }
            else { throw new SyntaxException("not number"); }

        }
        else if (a instanceof Double) {
            if (b instanceof Integer) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c + d;
            }
            else if (b instanceof Double) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c + d;
            }
            else if (b instanceof Complex) {
                Double c = (Double) a;
                return ((Complex) b).add(c);
            }
            else { throw new SyntaxException("not number"); }

        }
        else if (a instanceof Complex) {
            if (b instanceof Integer) {
                Complex c = (Complex) (a);
                Integer d = (Integer) (b);
                return c.add(d);
            }
            else if (b instanceof Double) {
                Complex c = (Complex) (a);
                Double d = (Double) (b);
                return c.add(d);
            }
            else if (b instanceof Complex) {
                Complex c = (Complex) (a);
                return ((Complex) b).add(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static @NotNull Object minus(@NotNull Object a, @NotNull Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c - d;
            }
            else if (b instanceof Double) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c - d;
            }
            else if (b instanceof Complex) {
                Integer c = (Integer) (a);
                Complex d = (Complex) b;
                return d.negate().add(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Double) {
            if (b instanceof Integer) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c - d;
            }
            else if (b instanceof Double) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c - d;
            }
            else if (b instanceof Complex) {
                Double c = (Double) (a);
                Complex d = (Complex) b;
                return d.negate().add(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Complex) {
            if (b instanceof Integer) {
                Complex c = (Complex) (a);
                Integer d = (Integer) (b);
                return c.negate().add(d).negate();
            }
            else if (b instanceof Double) {
                Complex c = (Complex) (a);
                Double d = (Double) (b);
                return c.negate().add(d).negate();
            }
            else if (b instanceof Complex) {
                Complex c = (Complex) (a);
                Complex d = (Complex) b;
                return c.negate().add(d).negate();
            }
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static @NotNull Object divide(@NotNull Object a, @NotNull Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c / d;
            }
            else if (b instanceof Double) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c / d;
            }
            else if (b instanceof Complex) {
                Integer c = (Integer) (a);
                Complex d = (Complex) (b);
                return new Complex(1).divide(d.divide(c));
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Double) {
            if (b instanceof Integer) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c / d;
            }
            else if (b instanceof Double) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c / d;
            }
            else if (b instanceof Complex) {
                Double c = (Double) (a);
                Complex d = (Complex) (b);
                return new Complex(1).divide(d.divide(c));
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Complex) {
            if (b instanceof Integer) {
                Complex c = (Complex) (a);
                Integer d = (Integer) (b);
                return c.divide(d);
            }
            else if (b instanceof Double) {
                Complex c = (Complex) (a);
                Double d = (Double) (b);
                return c.divide(d);
            }
            else if (b instanceof Complex) {
                Complex c = (Complex) (a);
                Complex d = (Complex) (b);
                return c.divide(d);
            }
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static @NotNull Object multiply(@NotNull Object a, @NotNull Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c * d;
            }
            else if (b instanceof Double) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c * d;
            }
            else if (b instanceof Complex) {
                Integer c = (Integer) (a);
                Complex d = (Complex) (b);
                return d.multiply(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Double) {
            if (b instanceof Integer) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c * d;
            }
            else if (b instanceof Double) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c * d;
            }
            else if (b instanceof Complex) {
                Double c = (Double) (a);
                Complex d = (Complex) (b);
                return d.multiply(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else if (a instanceof Complex) {
            if (b instanceof Integer) {
                Complex c = (Complex) (a);
                Integer d = (Integer) (b);
                return c.multiply(d);
            }
            else if (b instanceof Double) {
                Complex c = (Complex) (a);
                Double d = (Double) (b);
                return c.multiply(d);
            }
            else if (b instanceof Complex) {
                Complex c = (Complex) (a);
                Complex d = (Complex) (b);
                return d.multiply(c);
            }
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static double value(@NotNull Object o) {
        if (o instanceof Double) { return (Double) o; }
        else if (o instanceof Integer) { return (Integer) o; }
        else { throw new SyntaxException("not number"); }
    }

    static Optional<Complex> tryParseImaginary(@NotNull String s) {
        if(s.length() <= 1){
            return Optional.empty();
        }
        else if(!s.endsWith("i")){
            return Optional.empty();
        }
        else if(!Character.isDigit(s.charAt(s.length()-2))){
            return Optional.empty();
        }
        else{
            var img = s.substring(0,s.length()-1);
            var i = MagicUtils.tryParseInt(img);
            if(i.isPresent()){
                return Optional.of(new Complex(0,i.get()));
            }
            else{
                var d = tryParseDouble(img);
                return d.map(aDouble -> new Complex(0, aDouble));
            }
        }
    }


    /**
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se15/html/jls-3.html#jls-Digits">java 15 language specification 3.10.2. Floating-Point Literals</a>
     * @param s string
     * @return Double
     */
    static Optional<Double> tryParseDouble(@NotNull String s) {
        if(FloatingPointLiterals.parseFloatingPointLiteralSuccess(s)){
            return Optional.of(Double.parseDouble(s));
        }
        else{
            return Optional.empty();
        }
    }
}
