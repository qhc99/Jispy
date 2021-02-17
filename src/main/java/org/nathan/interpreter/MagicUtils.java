package org.nathan.interpreter;


public final class MagicUtils {

    public static boolean tryParseIntToArray(String s, int radix, int[] res)
    {
        if(res.length < 1){
            throw new RuntimeException("res length < 1");
        }

        if (s == null) {
            return false;
        }

        if (radix < Character.MIN_RADIX) {
            return false;
        }

        if (radix > Character.MAX_RADIX) {
            return false;
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
                } else if (firstChar != '+') {
                    return false;
                }

                if (len == 1) { // Cannot have lone "+" or "-"
                    return false;
                }
                i++;
            }
            int multmin = limit / radix;
            int result = 0;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                int digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0 || result < multmin) {
                    return false;
                }
                result *= radix;
                if (result < limit + digit) {
                    return false;
                }
                result -= digit;
            }
            res[0] = negative ? result : -result;
            return true;
        } else {
            return false;
        }
    }
}



