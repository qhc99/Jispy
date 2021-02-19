package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("IfStatementWithIdenticalBranches")
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
        if(first == '+' || first == '-'){
            idx++;
        }
        if(end()) return false;
        var c = s.charAt(idx);
        if (c == '.') {
            if (!isDigits()) {
                return false;
            }
            if (end()) { return true; }
            var c1 = s.charAt(idx);
            return ExponentPartOrFloatTypeSuffixOrBoth(c1);
        }
        else {
            if (!isDigits()) {
                return false;
            }
            if (end()) {
                return false;
            }
            var c3 = s.charAt(idx);
            switch (c3) {
                case '.' -> {
                    idx++;
                    if (end()) {
                        return true;
                    }
                    switch (s.charAt(idx)) {
                        case 'e', 'E' -> {
                            if (!isExponentPart()) {
                                return false;
                            }
                            if (end()) { return true; }
                            else { return isFloatTypeSuffix(); }
                        }
                        case 'f', 'F', 'd', 'D' -> {
                            return idx + 1 == s.length();
                        }
                        default -> {
                            if (!isDigits()) {
                                return false;
                            }
                            if (end()) { return true; }
                            var c2 = s.charAt(idx);
                            return ExponentPartOrFloatTypeSuffixOrBoth(c2);
                        }
                    }

                }
                case 'e', 'E' -> {
                    if (isExponentPart()) {
                        if (end()) {
                            return true;
                        }
                        return isFloatTypeSuffix();
                    }
                    else { return false; }
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
                if (!isExponentPart()) {
                    return false;
                }
                if (end()) { return true; }
                else { return isFloatTypeSuffix(); }
            }
            case 'f', 'F', 'd', 'D' -> {
                return idx + 1 == s.length();
            }
            default -> {return false;}
        }
    }

    private boolean isFloatTypeSuffix() {
        if (end()) {
            return false;
        }
        switch (s.charAt(idx)) {
            case 'f', 'F', 'd', 'D' -> {
                return idx + 1 == s.length();
            }
            default -> {return false;}
        }
    }

    private boolean isExponentPart() {
        if (end()) {
            return false;
        }
        var c = s.charAt(idx);
        switch (c) {
            case 'e', 'E' -> {
                idx++;
                if (end()) {
                    return false;
                }
                var c1 = s.charAt(idx);
                switch (c1) {
                    case '+':
                    case '-':
                        idx++;
                    default:
                        if (end()) {
                            return false;
                        }
                        return isDigits();
                }
            }
            default -> { return false; }
        }
    }

    private boolean isDigits() {
        if (end()) {
            return false;
        }
        if (!Character.isDigit(s.charAt(idx))) {
            return false;
        }
        idx++;
        if (end()) {
            return true;
        }
        while (idx < s.length()) {
            char c = s.charAt(idx);
            if (c == '_' || Character.isDigit(c)) {
                idx++;
            }
            else {
                break;
            }
        }
        return Character.isDigit(s.charAt(idx - 1));
    }

    private boolean end() {
        return idx >= s.length();
    }

}
