package org.nathan.interpreter;

import java.util.NoSuchElementException;

class LookUpException extends NoSuchElementException{
    LookUpException(String s) {
        super(s);
    }
}
