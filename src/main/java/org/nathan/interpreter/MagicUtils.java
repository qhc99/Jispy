/*
 * Copyright (c) 1994, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.nathan.interpreter;


import java.util.Optional;

/**
 * modified from open source library
 */
public final class MagicUtils {

    /**
     *
     * @see java.lang.Integer#parseInt(String, int)
     * @param s string
     * @param radix base
     * @return Optional Integer
     */
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



