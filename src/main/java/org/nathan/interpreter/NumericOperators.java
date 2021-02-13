package org.nathan.interpreter;

public class NumericOperators {

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
        if (a.getClass().equals(Integer.class)) { return -(Integer) a; }
        else if (a.getClass().equals(Double.class)) { return -(Double) a; }
        else { throw new ClassCastException(); }

    }

    static Object plus(Object a, Object b) {
        if (a.getClass().equals(Integer.class)) {
            if (b.getClass().equals(Integer.class)) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c + d;
            }
            else if (b.getClass().equals(Double.class)) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c + d;
            }
            else { throw new ClassCastException(); }

        }
        else if (a.getClass().equals(Double.class)) {
            if (b.getClass().equals(Integer.class)) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c + d;
            }
            else if (b.getClass().equals(Double.class)) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c + d;
            }
            else { throw new ClassCastException(); }

        }
        else { throw new ClassCastException(); }
    }


    static Object minus(Object a, Object b) {
        if (a.getClass().equals(Integer.class)) {
            if (b.getClass().equals(Integer.class)) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c - d;
            }
            else if (b.getClass().equals(Double.class)) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c - d;
            }
            else { throw new ClassCastException(); }
        }
        else if (a.getClass().equals(Double.class)) {
            if (b.getClass().equals(Integer.class)) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c - d;
            }
            else if (b.getClass().equals(Double.class)) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c - d;
            }
            else { throw new ClassCastException(); }
        }
        else { throw new ClassCastException(); }
    }

    static Object divide(Object a, Object b) {
        if (a.getClass().equals(Integer.class)) {
            if (b.getClass().equals(Integer.class)) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c / d;
            }
            else if (b.getClass().equals(Double.class)) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c / d;
            }
            else { throw new ClassCastException(); }
        }
        else if (a.getClass().equals(Double.class)) {
            if (b.getClass().equals(Integer.class)) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c / d;
            }
            else if (b.getClass().equals(Double.class)) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c / d;
            }
            else { throw new ClassCastException(); }
        }
        else { throw new ClassCastException(); }
    }

    static Object multiply(Object a, Object b) {
        if (a.getClass().equals(Integer.class)) {
            if (b.getClass().equals(Integer.class)) {
                Integer c = (Integer) (a);
                Integer d = (Integer) (b);
                return c * d;
            }
            else if (b.getClass().equals(Double.class)) {
                Integer c = (Integer) (a);
                Double d = (Double) (b);
                return c * d;
            }
            else { throw new ClassCastException(); }
        }
        else if (a.getClass().equals(Double.class)) {
            if (b.getClass().equals(Integer.class)) {
                Double c = (Double) (a);
                Integer d = (Integer) (b);
                return c * d;
            }
            else if (b.getClass().equals(Double.class)) {
                Double c = (Double) (a);
                Double d = (Double) (b);
                return c * d;
            }
            else { throw new ClassCastException(); }
        }
        else { throw new ClassCastException(); }
    }

    static double value(Object o) {
        if (o instanceof Double) { return (Double) o; }
        else if (o instanceof Integer) { return (Integer) o; }
        else { throw new ClassCastException(); }
    }
}
