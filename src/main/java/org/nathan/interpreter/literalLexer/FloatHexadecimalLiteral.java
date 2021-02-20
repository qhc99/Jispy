package org.nathan.interpreter.literalLexer;

import org.jetbrains.annotations.NotNull;

public class FloatHexadecimalLiteral extends LiteralLexer{

    public FloatHexadecimalLiteral(@NotNull String source){
        super(source);
    }

    public boolean parseSuccess(){
        if(s.startsWith("+") || s.startsWith("-")){
            if(!hasNext()){ return false; }
        }
        if(!s.startsWith("0x", idx) && !s.startsWith("0X", idx)){
            return false;
        }
        idx += 2;
        if(isChar('.')){
            if(!hasNext()){ return false; }
            if(!isHexDigits()){ return false; }
            return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot();
        }
        else{
            if(!isHexDigits()){ return false; }
            if(isChar('.')){
                if(!hasNext()){ return false; }
                if(!isHexDigits()){
                    return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot();
                }
                else{ return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot(); }
            }
            else{ return isBinaryExponentIndicatorWithFloatTypeSuffixOrNot(); }
        }
    }

    private boolean isBinaryExponentIndicatorWithFloatTypeSuffixOrNot(){
        if(isEnd()){
            return false;
        }
        switch(s.charAt(idx)){
            case 'p', 'P' -> {
                if(!hasNext()){ return false; }
                if(!isSignedInteger()){ return false; }
                if(isEnd()){ return true; }
                else{ return isFloatTypeSuffix(); }

            }
            default -> {
                return false;
            }
        }
    }

    protected boolean isChar(char c){
        return c == s.charAt(idx);
    }

    private boolean isHexDigits(){
        if(isEnd()){
            return false;
        }
        var c = s.charAt(idx);
        if(!isDigitOfHex(c)){
            return false;
        }
        if(!hasNext()){ return true; }
        while(!isEnd()) {
            var c1 = s.charAt(idx);
            if(c1 == '_' || isDigitOfHex(c1)){ idx++; }
            else{ break; }
        }
        return isDigitOfHex(s.charAt(idx - 1));
    }

    private static boolean isDigitOfHex(char c){
        switch(c){
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f',
                    'A', 'B', 'C', 'D', 'E', 'F' -> {
                return true;
            }
            default -> { return false; }
        }
    }

}
