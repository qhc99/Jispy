package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

public class HexadecimalFloatingPointLiteralParser {
    private final String s;
    private int idx;

    public HexadecimalFloatingPointLiteralParser(@NotNull String source) {
        s = source;
    }

    public boolean parseSuccess() {
        if (s.startsWith("+") || s.startsWith("-")) { next(); }
        if (isEnd()) { return false; }
        if (!s.startsWith("0x", idx) || !s.startsWith("0X", idx)) { return false; }
        idx += 2;
        if(isChar('.')){
            if(isNotHexDigits()) return false;
            return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot();
        }
        else{
            if(isNotHexDigits()) return false;
            if(isChar('.')){
                return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot();
            }
            return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot();
        }
    }

    private boolean isBinaryExponentIndicatorWithFloatTypeSuffixOrNot() {
        if (isEnd()) { return false; }
        switch (s.charAt(idx)) {
            case 'p', 'P' -> {
                next();
                if(isEnd()) return false;
                if(!isSignedInteger()) return false;
                if(isEnd()) return true;
                else return isFloatTypeSuffix();

            }
            default -> {return false;}
        }
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

    private void next(){
        idx++;
    }

    private boolean isChar(char c) {
        if (c == s.charAt(idx)) {
            next();
            return true;
        }
        return false;
    }

    private boolean isNotHexDigits() {
        if (isEnd()) { return true; }
        var c = s.charAt(idx);
        if (!isDigitOfHex(c)) { return true; }
        next();
        if (isEnd()) { return false; }
        while (!isEnd()) {
            var c1 = s.charAt(idx);
            if (c1 == '_' || isDigitOfHex(c1)) { next(); }
            else { break; }
        }
        return !isDigitOfHex(s.charAt(idx - 1));
    }

    private static boolean isDigitOfHex(char c) {
        switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f',
                    'A', 'B', 'C', 'D', 'E', 'F' -> {
                return true;
            }
            default -> { return false; }
        }
    }

    private boolean isEnd() {
        return idx >= s.length();
    }
}
