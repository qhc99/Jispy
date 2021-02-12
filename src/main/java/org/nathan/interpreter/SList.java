package org.nathan.interpreter;

import java.util.Iterator;


public class SList implements Iterable<Object> {
    Object Car;
    Object Cdr;

    SList(Object car) {
        Car = car;
        Cdr = Jispy.Nil;
    }


    SList(Object car, Object cdr) {
        Car = car;
        Cdr = cdr;
    }

    SList chainAdd(Object o){
        if(!(o.getClass().equals(SList.class))){
            Cdr = new SList(o);
            return (SList) Cdr;
        }
        else{
            throw new ClassCastException("can only add element");
        }
    }




    SList chainAppend(Object l){
        if(l.getClass().equals(SList.class)){
            if(Cdr.equals(Jispy.Nil)){
                Cdr = l;
                return (SList) (l);
            }
            else {
                SList cdr = (SList) (Cdr);
                return cdr.chainAppend(l);
            }
        }
        else{
            throw new ClassCastException("can only append list");
        }
    }

    boolean contentEqual(Object l){
        if(Car.equals(((SList)l).Car)){
            if(Cdr.getClass().equals(SList.class)){
                return ((SList)Cdr).contentEqual(((SList)l).Cdr);
            }
            else{
                return Cdr.equals(((SList) l).Cdr);
            }
        }
        return false;
    }

    int length(){
        if(Cdr.equals(Jispy.Nil)){
            return 1;
        }
        else{
            return 1 + ((SList)Cdr).length();
        }
    }

    @Override
    public String toString(){
        var s = new StringBuilder("[");
        for(var i : this){
            s.append(i);
            s.append(", ");
        }
        s.delete(s.length() - 2, s.length());
        s.append("]");
        return s.toString();
    }

    private static class SchemeListIterator implements Iterator<Object> {

        Object ptr;

        SchemeListIterator(Object l){
            ptr = l;
        }

        @Override
        public boolean hasNext() {
            return !ptr.equals(Jispy.Nil);
        }

        @Override
        public Object next() {
            var c = ((SList)ptr);
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

