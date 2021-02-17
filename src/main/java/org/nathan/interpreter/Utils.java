package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Utils {
    static boolean treeListEqual(List<Object> l1, List<Object> l2) {
        var i1 = l1.iterator();
        var i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            var item1 = i1.next();
            var item2 = i2.next();
            if (!(item1 instanceof List)) {
                if (!item1.equals(item2)) {
                    return false;
                }
            }
            else {
                if (!treeListEqual((List<Object>) (item1), (List<Object>) (item2))) {
                    return false;
                }
            }
        }
        return true;
    }

    static List<Object> treeList(Object... o) {
        return new ArrayList<>(Arrays.asList(o));
    }

    static boolean isNil(Object o) {
        if (o instanceof List) {
            return ((List<?>) o).isEmpty();
        }
        else {
            return false;
        }
    }

    static boolean stringContainsDigit(String s){
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }

    static boolean isTrue(Object o) {
        if (o instanceof Boolean) {return (Boolean) o;}
        else if (o == null) { return false; }
        else if (o instanceof Integer || o instanceof Double) { return !o.equals(0); }
        else { throw new SyntaxException("not bool"); }
    }

    public static boolean tryParseComplexToArray(String source, Complex[] res) {
        String[] parts = source.split("\\+");
        Double real = null, imaginary = null;
        if (parts.length == 0) {
            return false;
        }
        for (var part : parts) {
            var s = part.trim();
            if (s.contains("i")) {
                s = s.replaceAll("i", "");
                try{
                    imaginary = Double.parseDouble(s);
                }
                catch (NumberFormatException ignore){}
            }
            else {
                try{
                    real = Double.parseDouble(s);
                }
                catch (NumberFormatException ignore){}
            }
        }
        if(real == null && imaginary == null){
            return false;
        }
        else if (real == null) {
            res[0] = new Complex(0, imaginary);
            return true;
        }
        else if (imaginary == null) {
            res[0] = new Complex(real, 0);
            return true;
        }
        else {
            res[0] = new Complex(real, imaginary);
            return true;
        }
    }
}
