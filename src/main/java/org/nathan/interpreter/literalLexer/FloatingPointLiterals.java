package org.nathan.interpreter.literalLexer;

import org.jetbrains.annotations.NotNull;

public class FloatingPointLiterals {
    static boolean isFloatingPointLiteral(@NotNull String source){
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

    /**
     * @see
     * <a href="https://docs.oracle.com/javase/specs/jls/se15/html/jls-3.html#jls-Digits">java 15 specification</a>
     * @param source string to check
     * @return whether a double literal
     */
    public static boolean isDoubleParsable(@NotNull String source){
        return isFloatingPointLiteral(source);
    }

    /**
     * @see
     * <a href="https://docs.oracle.com/javase/specs/jls/se15/html/jls-3.html#jls-Digits">java 15 specification</a>
     * @param source string to check
     * @return whether a float literal
     */
    public static boolean isFloatParsable(@NotNull String source){
        return (source.endsWith("f") || source.endsWith("F")) && isFloatingPointLiteral(source);
    }
}
