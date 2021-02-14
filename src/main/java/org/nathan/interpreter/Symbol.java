package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Symbol {
    final String str;

    public Symbol(@NotNull String s) {
        this.str = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
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

    public static final Symbol _quote = new Symbol("quote");
    public static final Symbol _if = new Symbol("if");
    public static final Symbol _set = new Symbol("set!");
    public static final Symbol _define = new Symbol("define");
    public static final Symbol _lambda = new Symbol("lambda");
    public static final Symbol _begin = new Symbol("begin");
    public static final Symbol _define_macro = new Symbol("define-macro");
    public static final Symbol _quasi_quote = new Symbol("quasi-quote");
    public static final Symbol _unquote = new Symbol("unquote");
    public static final Symbol _unquote_splicing = new Symbol("unquote-splicing");
    public static final Symbol eof = new Symbol(";");
    public static final Map<String, Symbol> quotes = Map.of(
            "'", _quote,
            "`", _quasi_quote,
            ",", _unquote,
            ",@", _unquote_splicing);
    public static final Symbol _append = new Symbol("append");
    public static final Symbol _cons = new Symbol("cons");
    public static final Symbol _let = new Symbol("let");
    public static final Map<Symbol, Lambda> macro_table = new HashMap<>(Map.of(_let, Jispy::let));
}
