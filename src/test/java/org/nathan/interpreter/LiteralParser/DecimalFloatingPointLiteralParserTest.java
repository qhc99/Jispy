package org.nathan.interpreter.LiteralParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecimalFloatingPointLiteralParserTest {

    @Test
    void parseSuccess() {
        var t = new double[]{
                00.,
                01.,
                0____0.,
                0_0_0.0_0,
                00___0.e+0_0__0f,
                .0______0,
                .000e-000D,
                0_000E0001,
                1____1E0F,
                1_1_1D,
                0_0_0E0_0_0d,
        };
        assertNotNull(t);
        assertTrue(new DecimalFloatingPointLiteralParser("00.").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("01.").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("0____0.").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("0_0_0.0_0").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("00___0.e+0_0__0f").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser(".0______0").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser(".000e-000D").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("0_000E0001").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("1____1E0F").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("1_1_1D").parseSuccess());
        assertTrue(new DecimalFloatingPointLiteralParser("0_0_0E0_0_0d").parseSuccess());
    }
}