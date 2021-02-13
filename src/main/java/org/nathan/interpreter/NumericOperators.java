package org.nathan.interpreter;
import org.nathan.interpreter.Jispy.SyntaxException;

class NumericOperators {

    static boolean lessThan(Object a, Object b) {
        return value(a) < value(b);
    }

    static boolean lessOrEqual(Object a, Object b) {
        return value(a) <= value(b);
    }

    static boolean equal(Object a, Object b) {
        return value(a) == value(b);
    }

    static Object negative(Object a) {
        if (a instanceof Integer) { return -(Integer) a; }
        else if (a instanceof Double) { return -(Double) a; }
        else { throw new SyntaxException("not number"); }

    }

    static Object plus(Object a, Object b) {
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
            else { throw new SyntaxException("not number"); }

        }
        else { throw new SyntaxException("not number"); }
    }


    static Object minus(Object a, Object b) {
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
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static Object divide(Object a, Object b) {
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
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static Object multiply(Object a, Object b) {
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
            else { throw new SyntaxException("not number"); }
        }
        else { throw new SyntaxException("not number"); }
    }

    static double value(Object o) {
        if (o instanceof Double) { return (Double) o; }
        else if (o instanceof Integer) { return (Integer) o; }
        else { throw new SyntaxException("not number"); }
    }
}
