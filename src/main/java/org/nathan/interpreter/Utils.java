package org.nathan.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Utils {
    public static boolean treeListEqual(List<Object> l1, List<Object> l2) {
        var i1 = l1.iterator();
        var i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            var item1 = i1.next();
            var item2 = i2.next();
            if (!item1.getClass().equals(ArrayList.class)) {
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

    public static List<Object> treeList(Object... o) {
        return new ArrayList<>(Arrays.asList(o));
    }
}
