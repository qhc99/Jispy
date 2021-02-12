package org.nathan.interpreter;

public interface Procedure extends Lambda{
    default Object expression(){
        return null;
    }

    default Iterable<Object> parameters(){
        return null;
    }

    default Env environment(){
        return null;
    }
}
