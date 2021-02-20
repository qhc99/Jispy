package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

public abstract class Parser {
    protected final String s;
    protected int idx;
    protected Parser(@NotNull String s){
        this.s = s;
    }

    protected boolean isFloatTypeSuffix() {
        if (isEnd()) { return false; }
        switch (s.charAt(idx)) {
            case 'f', 'F', 'd', 'D' -> {
                return idx + 1 == s.length();
            }
            default -> {return false;}
        }
    }

    protected boolean isSignedInteger(){
        if(isEnd()) return false;
        switch (s.charAt(idx)) {
            case '+':
            case '-':
                if(!hasNext()) return false;
            default:
                return isDigits();
        }
    }

    protected boolean isDigits() {
        if (isEnd()) { return false; }
        if (!Character.isDigit(s.charAt(idx))) { return false; }
        if(!hasNext()) return true;
        while (idx < s.length()) {
            char c = s.charAt(idx);
            if (c == '_' || Character.isDigit(c)) { idx++; }
            else { break; }
        }
        return Character.isDigit(s.charAt(idx - 1));
    }

    protected boolean hasNext(){
        idx++;
        return idx < s.length();
    }

    protected boolean isEnd() {
        return idx >= s.length();
    }
}
