package org.nathan.interpreter;


import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.nathan.interpreter.Jispy.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.nathan.interpreter.Utils.*;

public class JispyTest {

    @Test
    public void parseTest() {
        List<Object> expected = treeList(new Symbol("begin"),
                treeList(new Symbol("define"), new Symbol("r"), 10),
                treeList(new Symbol("*"), new Symbol("pi"),
                        treeList(new Symbol("*"), new Symbol("r"), new Symbol("r"))));
        var t = (List<Object>) (parse("(begin (define r 10) (* pi (* r r)))"));
        assertTrue(Utils.treeListEqual(expected, t));
    }

    @Test
    public void beginTest() {
        assertEquals(314.1592653589793, Jispy.evalScripts("(begin (define r 10) (* pi (* r r)))"));
    }

    @Test
    public void ifTest() {
        assertEquals(42, Jispy.evalScripts("(if (> (* 11 11) 120) (* 7 6) oops)"));
    }

    @Test
    public void listTest() {
        var res = Jispy.evalScripts("(list (+ 1 1) (+ 2 2) (* 2 3) (expt 2 3))");
        var b = new ArrayList<>(treeList(2, 4, 6, 8.0));
        assertEquals(b, res);
    }

    @Test
    public void lambdaTest2() {
        assertEquals(13, Jispy.evalScripts("(fib 6)"));
    }

    @Test
    public void lambdaTest3() {
        var res = Jispy.evalScripts("(count 0 (list 0 1 2 3 0 0))");
        assertEquals(3, res);
    }

    @Test
    public void consTest() {
        var b = new ArrayList<>(treeList(1, 4, 9, 16));
        assertEquals(b, Jispy.evalScripts("(map square (range 1 5))"));
    }

    @Test
    public void mapTest() {
        List<Object> expected = new ArrayList<>(treeList(4, 6, 8, 10));
        assertEquals(expected, (Jispy.evalScripts("(begin " +
                "(define two (lambda (a b) (+ a b 2))) " +
                "(define l (list 1 2 3 4)) " +
                "(map two l l))")));
    }

    @Test
    public void appendTest() {
        var expected = new ArrayList<>(treeList(1, 2, 3, 4, 5, 6));
        var res = Jispy.evalScripts("(append (list 1 2) (list 3 4) (list 5 6))");
        assertEquals(expected, res);
    }

    @Test
    public void tailRecursionTest() {
        assertEquals(500500, Jispy.evalScripts("(sum2 1000 0)"));
    }

    @Test
    public void expandTest() {
        assertEquals(1000, Jispy.evalScripts("(cube 10)"));
    }

    @Test
    public void exceptionTest() {
        assertThrows(SyntaxException.class, () -> Jispy.evalScripts("(if 1 2 3 4 5)"));
    }

    @Test
    public void setTest() {
        assertEquals(3, Jispy.evalScripts("(begin (define x 1) (set! x (+ x 1)) (+ x 1))"));
    }

    @Test
    public void lispyTest() {
        assertEquals(treeList(new Symbol("testing"), 1, treeList(2.0), -3.14E159), evalScripts("(quote (testing 1 (2" +
                ".0) -3.14e159))"));
        assertEquals(4, evalScripts("(+ 2 2)"));
        assertEquals(210, evalScripts("(+ (* 2 100) (* 1 10))"));
        assertEquals(2, evalScripts("(if (> 6 5) (+ 1 1) (+ 2 2))"));
        assertEquals(4, evalScripts("(if (< 6 5) (+ 1 1) (+ 2 2))"));
        assertEquals(3, evalScripts("(begin (define x 3) x)"));
        assertEquals(6, evalScripts("(begin (define x 3) (+ x x))"));
        assertEquals(3, evalScripts("(begin (define x 1) (set! x (+ x 1)) (+ x 1))"));
        assertEquals(10, evalScripts("((lambda (x) (+ x x)) 5)"));
        assertEquals(10, evalScripts("(twice 5)"));
        assertEquals(treeList(10), evalScripts("((compose list twice) 5)"));
        assertEquals(80, evalScripts("((repeat (repeat twice)) 5)"));
        assertEquals(6, evalScripts("(fact 3)"));
        assertEquals(3628800, evalScripts("(fact 10)"));
        assertEquals(treeList(3, 0, 3), evalScripts("(list (abs -3) (abs 0) (abs 3)) ; => (3 0 3)"));
        assertEquals(treeList(treeList(1, 5), treeList(2, 6), treeList(3, 7), treeList(4, 8)), evalScripts("(zip " +
                "(list 1 2 3 4) (list 5 6 7 8))"));
        assertEquals(treeList(1, 5, 2, 6, 3, 7, 4, 8), evalScripts("(riff-shuffle (list 1 2 3 4 5 6 7 8))"));
        assertEquals(treeList(1, 3, 5, 7, 2, 4, 6, 8), evalScripts("((repeat riff-shuffle) (list 1 2 3 4 5 6 7 8))"));
        assertEquals(treeList(1, 2, 3, 4, 5, 6, 7, 8), evalScripts("(riff-shuffle (riff-shuffle (riff-shuffle (list 1" +
                " 2 3 4 5 6 7 8))))"));
        assertThrows(SyntaxException.class, () -> evalScripts("()"));
        assertThrows(SyntaxException.class, () -> evalScripts("(set! x)"));
        assertThrows(SyntaxException.class, () -> evalScripts("(define 3 4)"));
        assertThrows(SyntaxException.class, () -> evalScripts("(quote 1 2)"));
        assertThrows(SyntaxException.class, () -> evalScripts("(if 1 2 3 4)"));
        assertThrows(SyntaxException.class, () -> evalScripts("(lambda 3 3)"));
        assertThrows(SyntaxException.class, () -> evalScripts("(lambda (x))"));
        assertThrows(SyntaxException.class, () -> evalScripts("(if (= 1 2) (define-macro a 'a) \n" +
                "(define-macro a 'b))"));
        assertEquals(4, evalScripts("(twice 2)"));
        assertThrows(TypeException.class, () -> evalScripts("(twice 2 2)"));
        assertEquals(treeList(1, 2, 3, 4), evalScripts("(lyst 1 2 3 (+ 2 2))"));
        assertEquals(2, evalScripts("(if 1 2)"));
        assertNull(evalScripts("(if (= 3 4) 2)"));
        assertEquals(treeList(100, 110, 120), evalScripts("(begin " +
                "(define ((account bal) amt) (set! bal (+ bal amt)) bal) " +
                "(define a1 (account 100)) (list (a1 0) (a1 10) (a1 10)))"));
        assertTrue((Boolean) evalScripts("(> (square-root 200.) 14.14213)"));
        assertTrue((Boolean) evalScripts("(< (square-root 200.) 14.14215)"));
        assertTrue((Boolean) evalScripts("(= (square-root 200.) (sqrt 200.))"));
        assertEquals(9045050, evalScripts("(sum-squares-range 1 300)"));
        assertEquals(1, evalScripts("(call/cc (lambda (throw) (+ 5 (* 10 (throw 1)))))"));
        assertEquals(15, evalScripts("(call/cc (lambda (throw) (+ 5 (* 10 1))))"));
        assertEquals(35, evalScripts("(call/cc (lambda (throw) " +
                "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (escape 3))))))))"));
        assertEquals(3, evalScripts("(call/cc (lambda (throw) " +
                "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (throw 3))))))))"));
        assertEquals(1005, evalScripts("(call/cc (lambda (throw) " +
                "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 1)))))))"));
        assertEquals(new Complex(-1, 0), evalScripts("(* 1i 1i)"));
        assertEquals(new Complex(0, 1), evalScripts("(sqrt -1)"));
        assertEquals(3, evalScripts("(let ((a 1) (b 2)) (+ a b))"));
        assertThrows(SyntaxException.class, () -> evalScripts("(let ((a 1) (b 2 3)) (+ a b))"));
        assertEquals(3, evalScripts("(and 1 2 3)"));
        assertEquals(3, evalScripts("(and (> 2 1) 2 3)"));
        assertTrue((Boolean) evalScripts("(and)"));
        assertFalse((Boolean) evalScripts("(and (> 2 1) (> 2 3))"));
        assertNull(evalScripts("(unless (= 2 (+ 1 1)) (display 2) 3 4)"));
        assertEquals(2, evalScripts("2"));
        assertEquals(4, evalScripts("(unless (= 4 (+ 1 1)) (display 2) (display 'n) 3 4)"));
        assertEquals(new Symbol("x"), evalScripts("(quote x)"));
        assertEquals(treeList(1, 2, new Symbol("three")), evalScripts("(quote (1 2 three))"));
        assertEquals(new Symbol("x"), evalScripts("'x"));
        assertEquals(treeList(new Symbol("one"), 2, 3), evalScripts("'(one 2 3)"));
        assertEquals(treeList(treeList(new Symbol("testing"), 1, 2, 3, new Symbol("testing")), treeList(new Symbol("testing"), treeList(1,2,3), new Symbol("testing"))),
                evalScripts("(begin (define L (list 1 2 3)) (list `(testing ,@L testing) `(testing ,L testing) ) ) "));
        assertThrows(SyntaxException.class, () -> evalScripts("(begin (define L (list 1 2 3)) `,@L)"));
        assertEquals(treeList(1, 2, 3), evalScripts("""
                '(1 ; test comments '\s
                     ; skip this line
                     2 ;  more ;  comments ;  ) )
                     3) ;  final comment  ; => (1 2 3)"""));
    }
}
