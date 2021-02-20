package org.nathan.interpreter.LiteralParser;

import org.jetbrains.annotations.NotNull;

/**
 * Regex:
 * <br/>Digits:(\d[\d_]+\d|\d)
 * <br/>ExponentPart: ([eE][+-]?(\d[\d_]+\d|\d))
 * <br/>FloatTypeSuffix: ([fFdD])
 * <br/>Double Type1: ([+-]?(\d[\d_]+\d|\d)\.(\d[\d_]+\d|\d)?([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)
 * <br/>Double Type2: ([+-]\.(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)
 * <br/>Double Type3: ([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))([fFdD])?)
 * <br/>Double Type4: ([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD]))
 * <br/>All Double: (([+-]?(\d[\d_]+\d|\d)\.(\d[\d_]+\d|\d)?([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)|([+-]\.(\d[\d_]+\d|\d)
 * <br/>([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)|([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))([fFdD])?)|([+-]?
 * <br/>(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])))
 */
public class FloatingPointLiteralsParser {
    public static boolean parseFloatingPointLiteralSuccess(@NotNull String source){
        if(source.startsWith("0x") || source.startsWith("0X")){
            return new HexadecimalFloatingPointLiteralParser(source).parseSuccess();
        }
        else{
            return new DecimalFloatingPointLiteralParser(source).parseSuccess();
        }
    }
}
