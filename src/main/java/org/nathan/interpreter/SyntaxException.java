package org.nathan.interpreter;

class SyntaxException extends RuntimeException {

    public SyntaxException(String s) {
        super(s);
    }
}

class RuntimeWarning extends RuntimeException {
    Object returnValue;

    RuntimeWarning(String m) {
        super(m);
    }
}

class ArgumentsCountException extends RuntimeException {
    public ArgumentsCountException() {
        super();
    }
}
