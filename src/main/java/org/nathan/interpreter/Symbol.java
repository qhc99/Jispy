package org.nathan.interpreter;

import java.util.Map;

record Symbol(String Value){

    static final Symbol _quote = new Symbol("quote");
    static final Symbol _if = new Symbol("if");
    static final Symbol _set = new Symbol("set!");
    static final Symbol _define = new Symbol("define");
    static final Symbol _lambda = new Symbol("lambda");
    static final Symbol _begin = new Symbol("begin");
    static final Symbol _define_macro = new Symbol("define-macro");
    static final Symbol _quasi_quote = new Symbol("quasi-quote");
    static final Symbol _unquote = new Symbol("unquote");
    static final Symbol _unquote_splicing = new Symbol("unquote-splicing");
    static final Symbol eof = new Symbol("#<symbol-eof>");
    static final Map<String, Symbol> quotes = Map.of(
            "'", _quote,
            "`", _quasi_quote,
            ",", _unquote,
            ",@", _unquote_splicing);
    static final Symbol _append = new Symbol("append");
    static final Symbol _cons = new Symbol("cons");
    static final Symbol _let = new Symbol("let");
}
