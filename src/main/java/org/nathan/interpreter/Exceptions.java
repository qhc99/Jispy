package org.nathan.interpreter;

import java.util.NoSuchElementException;

class Exceptions{
    static class SyntaxException extends RuntimeException {

        SyntaxException(String s) {
            super(s);
        }
    }

    static class RuntimeWarning extends RuntimeException {
        Object returnValue;

        RuntimeWarning(String m) {
            super(m);
        }
    }

    static class ArgumentsCountException extends IllegalArgumentException {
        ArgumentsCountException() {
            super();
        }
    }

    static class LookUpException extends NoSuchElementException{
        LookUpException(String s) {
            super(s);
        }
    }

    static class TypeException extends RuntimeException {
        TypeException(String s) {
            super(s);
        }
    }
}
