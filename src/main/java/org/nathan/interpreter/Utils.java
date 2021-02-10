package org.nathan.interpreter;

public class Utils {
    static Object convertToAtom(String x) {
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

    @SuppressWarnings("unchecked")
    static <T> T convert(Object o) {
        return (T) o;
    }

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
}
