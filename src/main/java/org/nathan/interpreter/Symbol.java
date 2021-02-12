package org.nathan.interpreter;

import java.util.Map;
import java.util.Objects;

public class Symbol {
    final String str;

    public Symbol(String s) {
        this.str = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass() && !o.getClass().equals(String.class))) {
            return false;
        }
        if (o.getClass().equals(String.class)) {
            return str.equals(o);
        }
        Symbol symbol = (Symbol) o;
        return str.equals(symbol.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    public String toString() {
        return str;
    }

    static Symbol _quote = new Symbol("quote");
    static Symbol _if = new Symbol("if");
    static Symbol _set = new Symbol("set!");
    static Symbol _define =  new Symbol("define");
    static Symbol _lambda =  new Symbol("lambda");
    static Symbol _begin = new Symbol("begin");
    static Symbol _define_macro =  new Symbol("define-macro");
    static Symbol _quasi_quote =  new Symbol("quasi-quote");
    static Symbol _unquote =  new Symbol("unquote");
    static Symbol _unquote_splicing =  new Symbol("unquote-splicing");
    static Symbol eof = new Symbol("#<eof-object>");
    static Map<String, Symbol> quotes = Map.of(
            "'", _quote,
            "`", _quasi_quote,
            ",", _unquote,
            ",@", _unquote_splicing);
    static Symbol _append = new Symbol("append");
    static Symbol _cons = new Symbol("cons");
    static Symbol _let = new Symbol("let");
    static Map<Symbol, Lambda> macro_table = Map.of(_let, Jispy::let);
}
