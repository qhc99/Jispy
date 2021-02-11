package org.nathan.interpreter;

class SchemeListBuilder {

    private final SchemeList head;
    private SchemeList ptr;

    SchemeListBuilder() {
        head = new SchemeList(Jisp.Nil);
        ptr = head;
    }

    SchemeListBuilder append(Object o) {
        if (head.Car.equals(Jisp.Nil)) {
            head.Car = o;
        }
        else {
            ptr = ptr.chainAdd(o);
        }
        return this;
    }

    SchemeList toSchemeList() {
        return head;
    }
}
