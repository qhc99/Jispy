package org.nathan.interpreter;

import static org.nathan.interpreter.Jispy.*;

class SListBuilder {

    private final SList head;
    private SList ptr;

    SListBuilder() {
        head = new SList(Nil);
        ptr = head;
    }

    SListBuilder append(Object o) {
        if (head.Car.equals(Nil)) {
            head.Car = o;
        }
        else {
            ptr = ptr.chainAdd(o);
        }
        return this;
    }

    SList toSchemeList() {
        return head;
    }
}
