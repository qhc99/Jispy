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
        return Objects.equals(str, symbol.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    public String toString() {
        return str;
    }

    static Map<String, Symbol> _quote = Map.of("quote", new Symbol("quote"));
    static Map<String, Symbol> _if = Map.of("if", new Symbol("if"));
    static Map<String, Symbol> _set = Map.of("set!", new Symbol("set!"));
    static Map<String, Symbol> _define = Map.of("define", new Symbol("define"));
    static Map<String, Symbol> _lambda = Map.of("lambda", new Symbol("lambda"));
    static Map<String, Symbol> _begin = Map.of("begin", new Symbol("begin"));
    static Map<String, Symbol> _define_macro = Map.of("define-macro", new Symbol("define-macro"));
    static Map<String, Symbol> _quasi_quote = Map.of("quasi-quote", new Symbol("quasi-quote"));
    static Map<String, Symbol> _unquote = Map.of("unquote", new Symbol("unquote"));
    static Map<String, Symbol> _unquote_splicing = Map.of("unquote-splicing", new Symbol("unquote-splicing"));
    static Symbol eof = new Symbol("#<eof-object>");
    static Map<String, Map<String, Symbol>> quotes = Map.of(
            "'", _quote,
            "`", _quasi_quote,
            ",", _unquote,
            ",@", _unquote_splicing);
}
