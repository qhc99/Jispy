package org.nathan.interpreter.literalParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloatDecimalLiteralTest {

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
                -3.14E159,
                2.0,
                11.f
        };
        assertNotNull(t);
        assertTrue(new FloatDecimalLiteral("00.").parseSuccess());
        assertTrue(new FloatDecimalLiteral("01.F").parseSuccess());
        assertTrue(new FloatDecimalLiteral("0____0.").parseSuccess());
        assertTrue(new FloatDecimalLiteral("0_0_0.0_0").parseSuccess());
        assertTrue(new FloatDecimalLiteral("00___0.e+0_0__0f").parseSuccess());
        assertTrue(new FloatDecimalLiteral(".0______0").parseSuccess());
        assertTrue(new FloatDecimalLiteral(".000e-000D").parseSuccess());
        assertTrue(new FloatDecimalLiteral("0_000E0001").parseSuccess());
        assertTrue(new FloatDecimalLiteral("1____1E0F").parseSuccess());
        assertTrue(new FloatDecimalLiteral("1_1_1D").parseSuccess());
        assertTrue(new FloatDecimalLiteral("0_0_0E0_0_0d").parseSuccess());
        assertTrue(new FloatDecimalLiteral("-3.14E159").parseSuccess());
        assertTrue(new FloatDecimalLiteral("2.0").parseSuccess());
        assertTrue(new FloatDecimalLiteral("11.f").parseSuccess());
        assertTrue(new FloatDecimalLiteral("11f").parseSuccess());

        assertFalse(new FloatDecimalLiteral("").parseSuccess());
        assertFalse(new FloatDecimalLiteral("0__F.EF").parseSuccess());
        assertFalse(new FloatDecimalLiteral(".11a").parseSuccess());
        assertFalse(new FloatDecimalLiteral(".11DD").parseSuccess());
    }
}