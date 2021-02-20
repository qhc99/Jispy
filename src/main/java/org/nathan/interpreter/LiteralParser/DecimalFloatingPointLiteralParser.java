package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

public class DecimalFloatingPointLiteralParser {
    private final String s;
    private int idx;

    public DecimalFloatingPointLiteralParser(@NotNull String source) {
        s = source;
    }

    public boolean parseSuccess() {
        if (s.length() == 0) {
            return false;
        }
        var first = s.charAt(idx);
        if (first == '+' || first == '-') { next(); }
        if (isEnd()) { return false; }
        var c = s.charAt(idx);
        if (c == '.') {
            next();
            if (!isDigits()) { return false; }
            if (isEnd()) { return true; }
            var c1 = s.charAt(idx);
            return ExponentPartOrFloatTypeSuffixOrBoth(c1);
        }
        else {
            if (!isDigits()) { return false; }
            if (isEnd()) { return false; }
            var c3 = s.charAt(idx);
            switch (c3) {
                case '.' -> {
                    next();
                    if (isEnd()) { return true; }
                    switch (s.charAt(idx)) {
                        case 'e', 'E' -> {
                            next();
                            if(isEnd()) return false;
                            if(!isSignedInteger()) return false;
                            if (isEnd()) { return true; }
                            else { return isFloatTypeSuffix(); }
                        }
                        case 'f', 'F', 'd', 'D' -> {
                            return idx + 1 == s.length();
                        }
                        default -> {
                            if (!isDigits()) { return false; }
                            if (isEnd()) { return true; }
                            var c2 = s.charAt(idx);
                            return ExponentPartOrFloatTypeSuffixOrBoth(c2);
                        }
                    }

                }
                case 'e', 'E' -> {
                    next();
                    if(isEnd()) return false;
                    if(isSignedInteger()){
                        if (isEnd()) { return true; }
                        return isFloatTypeSuffix();
                    }
                    else return false;
                }
                case 'f', 'F', 'd', 'D' -> {
                    return idx + 1 == s.length();
                }
                default -> {return false;}
            }
        }
    }

    private boolean ExponentPartOrFloatTypeSuffixOrBoth(char c1) {
        switch (c1) {
            case 'e', 'E' -> {
                next();
                if(isEnd()) return false;
                if(!isSignedInteger()) return false;
                if (isEnd()) { return true; }
                else { return isFloatTypeSuffix(); }
            }
            case 'f', 'F', 'd', 'D' -> {
                return idx + 1 == s.length();
            }
            default -> {return false;}
        }
    }

    private void next(){
        idx++;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean isFloatTypeSuffix() {
        if (isEnd()) { return false; }
        switch (s.charAt(idx)) {
            case 'f', 'F', 'd', 'D' -> {
                return idx + 1 == s.length();
            }
            default -> {return false;}
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean isSignedInteger(){
        if(isEnd()) return false;
        switch (s.charAt(idx)) {
            case '+':
            case '-':
                next();
            default:
                if (isEnd()) { return false; }
                return isDigits();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean isDigits() {
        if (isEnd()) { return false; }
        if (!Character.isDigit(s.charAt(idx))) { return false; }
        next();
        if (isEnd()) { return true; }
        while (idx < s.length()) {
            char c = s.charAt(idx);
            if (c == '_' || Character.isDigit(c)) { next(); }
            else { break; }
        }
        return Character.isDigit(s.charAt(idx - 1));
    }

    private boolean isEnd() {
        return idx >= s.length();
    }

}
