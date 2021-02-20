package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

public class HexadecimalFloatingPointLiteralParser {
    private final String s;
    private int idx;

    public HexadecimalFloatingPointLiteralParser(@NotNull String source) {
        s = source;
    }

    public boolean parseSuccess() {
        if (!s.startsWith("0x") || !s.startsWith("0X")) { return false; }
        idx += 2;
        if (end()) { return false; }
        if (s.charAt(idx) == '.') {
            if (isHexDigits()) {
                return end();
            }
            else { return false; }
        }
        else {
            if (isHexDigits()) {
                if (end()) { return true; }
                if (!(s.charAt(idx) == '.')) { return false; }
                idx++;
                if (end()) { return false; }
                if (isHexDigits()) {
                    return end();
                }
                else { return false; }
            }
            else { return false; }
        }
    }

    private boolean isHexDigits() {
        if (end()) { return false; }
        var c = s.charAt(idx);
        if (!isDigitOfHex(c)) { return false; }
        idx++;
        if (end()) { return true; }
        while (!end()) {
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

    private boolean end() {
        return idx >= s.length();
    }
}
