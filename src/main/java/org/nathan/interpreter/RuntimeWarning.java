package org.nathan.interpreter;

class RuntimeWarning extends RuntimeException {
    Object returnValue;

    RuntimeWarning(String m) {
        super(m);
    }
}
