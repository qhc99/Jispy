package org.nathan.interpreter.literalLexer;

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
public class FloatingPointLiterals {
    public static boolean isFloatingPointLiteral(@NotNull String source){
        if(source.length() <= 1) return false;
        else if(source.length() == 2) return new FloatDecimalLiteral(source).isTheLiteral();
        if(source.startsWith("+") || source.startsWith("-")){
            if(source.startsWith("0x", 1) || source.startsWith("0X", 1)){
                return new FloatHexadecimalLiteral(source).isTheLiteral();
            }
            else{
                return new FloatDecimalLiteral(source).isTheLiteral();
            }
        }
        else{
            if(source.startsWith("0x") || source.startsWith("0X")){
                return new FloatHexadecimalLiteral(source).isTheLiteral();
            }
            else{
                return new FloatDecimalLiteral(source).isTheLiteral();
            }
        }
    }
}
