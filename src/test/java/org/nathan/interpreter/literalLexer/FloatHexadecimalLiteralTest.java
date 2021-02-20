package org.nathan.interpreter.literalLexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloatHexadecimalLiteralTest {

    @Test
    void parseSuccess() {
        var d = new double[]{
                0xa_fp-2_2f,
                0x0a_1.p+02_02,
                0x10F.0__1fp-0f,
                0x.1B0P-2_2D
        };
        assertNotNull(d);
        assertTrue(new FloatHexadecimalLiteral("0xa_fp-2_2f").isTheLiteral());
        assertTrue(new FloatHexadecimalLiteral("0x0a_1.p+02_02").isTheLiteral());
        assertTrue(new FloatHexadecimalLiteral("0x10F.0__1fp-0f").isTheLiteral());
        assertTrue(new FloatHexadecimalLiteral("0x.1B0P-2_2D").isTheLiteral());

        assertFalse(new FloatHexadecimalLiteral("+").isTheLiteral());
        assertFalse(new FloatHexadecimalLiteral("0x.1B0").isTheLiteral());
        assertFalse(new FloatHexadecimalLiteral("0xAAG").isTheLiteral());
        assertFalse(new FloatHexadecimalLiteral("00").isTheLiteral());
    }
}