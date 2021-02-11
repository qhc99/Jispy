package org.nathan.interpreter;

public class Utils {


    @SuppressWarnings("all")
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
