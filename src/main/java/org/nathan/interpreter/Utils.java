package org.nathan.interpreter;

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

    static boolean tryParseDoubleToArray(String s, double[] res){
        if(res.length < 1){
            throw new RuntimeException("res length < 1");
        }
        try {
            double t = Double.parseDouble(s);
            res[0] = t;
            return true;
        }
        catch (NumberFormatException ignore) {
        }
        return false;
    }

    static boolean isTrue(Object o) {
        if (o instanceof Boolean) {return (Boolean) o;}
        else if (o == null) { return false; }
        else if (o instanceof Integer || o instanceof Double) { return !o.equals(0); }
        else { throw new SyntaxException("not bool"); }
    }


}
