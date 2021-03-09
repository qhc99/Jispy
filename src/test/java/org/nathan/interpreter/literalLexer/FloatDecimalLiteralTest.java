package org.nathan.interpreter.literalLexer;

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
                11.f,
                11f,
                11E1,
                11.e1
        };
        assertNotNull(t);
        assertTrue(new FloatDecimalLiteral("00.").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("01.F").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("0____0.").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("0_0_0.0_0").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("00___0.e+0_0__0f").isTheLiteral());
        assertTrue(new FloatDecimalLiteral(".0______0").isTheLiteral());
        assertTrue(new FloatDecimalLiteral(".000e-000D").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("0_000E0001").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("1____1E0F").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("1_1_1D").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("0_0_0E0_0_0d").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("-3.14E159").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("2.0").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("11.f").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("11f").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("11E1").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("11.E1").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("Infinity").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("+Infinity").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("-Infinity").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("NaN").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("+NaN").isTheLiteral());
        assertTrue(new FloatDecimalLiteral("-NaN").isTheLiteral());

        assertFalse(new FloatDecimalLiteral("").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("0__F.EF").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".11a").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".11DD").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("+").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".M").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("0_00").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11E").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11EG").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11K").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".11E").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".11EJ").isTheLiteral());
        assertFalse(new FloatDecimalLiteral(".11B").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11.E").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11.EJ").isTheLiteral());
        assertFalse(new FloatDecimalLiteral("11.G").isTheLiteral());

    }
}