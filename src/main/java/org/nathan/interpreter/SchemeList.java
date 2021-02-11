package org.nathan.interpreter;

import java.util.Iterator;

import static org.nathan.interpreter.Utils.convert;

public class SchemeList implements Iterable<Object> {
    Object Car;
    Object Cdr;

    SchemeList(Object car) {
        Car = car;
        Cdr = Jisp.Nil;
    }


    SchemeList(Object car, Object cdr) {
        Car = car;
        Cdr = cdr;
    }

    SchemeList chainAdd(Object o) {
        if (!(o.getClass().equals(SchemeList.class))) {
            Cdr = new SchemeList(o);
            return (SchemeList) Cdr;
        }
        else {
            throw new ClassCastException("can only add element");
        }
    }


    SchemeList chainAppend(Object l) {
        if (l.getClass().equals(SchemeList.class)) {
            if (Cdr.equals(Jisp.Nil)) {
                Cdr = l;
                return convert(l);
            }
            else {
                SchemeList cdr = convert(Cdr);
                return cdr.chainAppend(l);
            }
        }
        else {
            throw new ClassCastException("can only append list");
        }
    }

    boolean contentEqual(Object l) {
        if (Car.equals(((SchemeList) l).Car)) {
            if (Cdr.getClass().equals(SchemeList.class)) {
                return ((SchemeList) Cdr).contentEqual(((SchemeList) l).Cdr);
            }
            else {
                return Cdr.equals(((SchemeList) l).Cdr);
            }
        }
        return false;
    }

    int length() {
        if (Cdr.equals(Jisp.Nil)) {
            return 1;
        }
        else {
            return 1 + ((SchemeList) Cdr).length();
        }
    }

    @Override
    public String toString() {
        var s = new StringBuilder("[");
        for (var i : this) {
            s.append(i);
            s.append(", ");
        }
        s.delete(s.length() - 2, s.length());
        s.append("]");
        return s.toString();
    }

    private static class SchemeListIterator implements Iterator<Object> {

        Object ptr;

        SchemeListIterator(Object l) {
            ptr = l;
        }

        @Override
        public boolean hasNext() {
            return !ptr.equals(Jisp.Nil);
        }

        @Override
        public Object next() {
            var c = ((SchemeList) ptr);
            var r = c.Car;
            ptr = c.Cdr;
            return r;
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return new SchemeListIterator(this);
    }
}

