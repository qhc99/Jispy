package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

public class HexadecimalFloatingPointLiteralParser {
    private final String s;
    private int idx;

    public HexadecimalFloatingPointLiteralParser(@NotNull String source) {
        s = source;
    }

    public boolean parseSuccess() {
        if(s.startsWith("+") || s.startsWith("-")) idx++;
        if(isEnd()) return false;
        if (!s.startsWith("0x", idx) || !s.startsWith("0X", idx)) { return false; }
        idx += 2;
        if (isEnd()) { return false; }
        if (isChar('.')) {
            if(isEnd()) return false;
            if (isHexDigits()) { return isEnd(); }
            else { return false; }
        }
        else {
            if (isHexDigits()) {
                if (isEnd()) { return true; }
                else if (!isChar('.')) { return false; }
                if (isEnd()) { return false; }
                if (isHexDigits()) { return isEnd(); }
                else { return false; }
            }
            else { return false; }
        }
    }

    private boolean isChar(char c){
        if(c == s.charAt(idx)){
            idx++;
            return true;
        }
        return false;
    }

    private boolean isHexDigits() {
        if (isEnd()) { return false; }
        var c = s.charAt(idx);
        if (!isDigitOfHex(c)) { return false; }
        idx++;
        if (isEnd()) { return true; }
        while (!isEnd()) {
            var c1 = s.charAt(idx);
            if (c1 == '_' || isDigitOfHex(c1)) { idx++; }
            else { break; }
        }
        return isDigitOfHex(s.charAt(idx - 1));
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
