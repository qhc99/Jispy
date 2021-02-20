package org.nathan.interpreter.literalParser;

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
        assertTrue(new FloatHexadecimalLiteral("0xa_fp-2_2f").parseSuccess());
        assertTrue(new FloatHexadecimalLiteral("0x0a_1.p+02_02").parseSuccess());
        assertTrue(new FloatHexadecimalLiteral("0x10F.0__1fp-0f").parseSuccess());
        assertTrue(new FloatHexadecimalLiteral("0x.1B0P-2_2D").parseSuccess());

        assertFalse(new FloatHexadecimalLiteral("+").parseSuccess());
        assertFalse(new FloatHexadecimalLiteral("0x.1B0").parseSuccess());
        assertFalse(new FloatHexadecimalLiteral("0xAAG").parseSuccess());
    }
}