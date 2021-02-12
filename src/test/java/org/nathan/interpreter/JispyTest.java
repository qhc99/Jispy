package org.nathan.interpreter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.nathan.interpreter.Utils.*;
import static org.nathan.interpreter.Jispy.*;
import static org.junit.jupiter.api.Assertions.*;

public class JispyTest {
    static boolean listTreeEqual(List<Object> l1, List<Object> l2) {
        var i1 = l1.iterator();
        var i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            var item1 = i1.next();
            var item2 = i2.next();
            if (!item1.getClass().equals(ArrayList.class)) {
                if (!item1.equals(item2)) {
                    return false;
                }
            }
            else {
                if (!listTreeEqual(convert(item1), convert(item2))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void parseTest() {
        List<Object> expected = Arrays.asList("begin",
                Arrays.asList("define", "r", 10),
                Arrays.asList("*", "pi", Arrays.asList("*","r","r")));
        assertTrue(listTreeEqual(expected, convert(parse("(begin (define r 10) (* pi (* r r)))"))));
    }

    @Test
    public void caseTest1() {
        assertEquals(314.1592653589793, runScheme("(begin (define r 10) (* pi (* r r)))"));
    }

    @Test
    public void caseTest2() {
        assertEquals(42, runScheme("(if (> (* 11 11) 120) (* 7 6) oops)"));
    }

    @Test
    public void caseTest3() {
        var res = runScheme("(list (+ 1 1) (+ 2 2) (* 2 3) (expt 2 3))");
        var b = new SListBuilder();
        b.append(2).append(4).append(6).append(8.0);
        assertTrue(b.toSchemeList().contentEqual(convert(res)));
    }

    @Test
    public void caseTest4() {
        assertEquals(120, runScheme("(begin " +
                "(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1)))))) " +
                "(fact 5))"));
    }

    @Test
    public void caseTest5() {
        assertEquals(13, runScheme("(begin " +
                "(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))) " +
                "(fib 6))"));
    }

    @Test
    public void caseTest6() {
        assertEquals(3, runScheme("(begin " +
                "(define count (lambda (item L) (if (null? L) 0 (+ (if (equal? item (car L)) 1 0) (count item (cdr L)))))) " +
                "(count 0 (list 0 1 2 3 0 0)))"));
    }

    @Test
    public void caseTest7() {
        var b = new SListBuilder();
        b.append(1).append(4).append(9).append(16);
        assertTrue(b.toSchemeList().contentEqual(runScheme("(begin " +
                "(define square (lambda (x) (* x x))) " +
                "(define range (lambda (a b) (if (= a b) nil (cons a (range (+ a 1) b))))) " +
                "(map square (range 1 5)))")));
    }

    @Test
    public void caseTest8() {
        var expected = new SListBuilder();
        expected.append(4).append(6).append(8).append(10);
        assertTrue(expected.toSchemeList().contentEqual(runScheme("(begin " +
                "(define two (lambda (a b) (+ a b 2))) " +
                "(define l (list 1 2 3 4)) " +
                "(map two l l))")));
    }

    @Test
    public void caseTest9(){
        var expected = new SListBuilder();
        expected.append(1).append(2).append(3).append(4).append(5).append(6);
        var res = runScheme("(append (list 1 2) (list 3 4) (list 5 6))");
        assertTrue(expected.toSchemeList().contentEqual(res));

    }
}
