package org.nathan.interpreter;


import java.util.Optional;

public final class MagicUtils {

    public static Optional<Integer> tryParseInt(String s, int radix) {

        if (s == null) {
            return Optional.empty();
        }

        if (radix < Character.MIN_RADIX) {
            return Optional.empty();
        }

        if (radix > Character.MAX_RADIX) {
            return Optional.empty();
        }

        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                }
                else if (firstChar != '+') {
                    return Optional.empty();
                }

                if (len == 1) { // Cannot have lone "+" or "-"
                    return Optional.empty();
                }
                i++;
            }
            int multmin = limit / radix;
            int result = 0;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                int digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0 || result < multmin) {
                    return Optional.empty();
                }
                result *= radix;
                if (result < limit + digit) {
                    return Optional.empty();
                }
                result -= digit;
            }
            return Optional.of(negative ? result : -result);
        }
        else {
            return Optional.empty();
        }
    }
}



