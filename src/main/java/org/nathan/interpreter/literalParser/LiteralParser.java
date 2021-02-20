package org.nathan.interpreter.literalParser;

import org.jetbrains.annotations.NotNull;

public abstract class LiteralParser {
    protected final String s;
    protected int idx;
    protected LiteralParser(@NotNull String s){
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
