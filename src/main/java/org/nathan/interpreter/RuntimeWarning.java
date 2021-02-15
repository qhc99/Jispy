package org.nathan.interpreter;

class RuntimeWarning extends RuntimeException {
    public Object returnValue;

    public RuntimeWarning(String m) {
        super(m);
    }
}
