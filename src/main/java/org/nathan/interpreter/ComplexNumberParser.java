package org.nathan.interpreter;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import java.text.ParsePosition;

public final class ComplexNumberParser extends ComplexFormat {

    static String tryMathExpToComplex(String exp) {
        String[] parts = exp.split("\\+");
        Double real = null, imaginary = null;
        if (parts.length == 0) {
            throw new NumberFormatException("exactly +");
        }
        for (var part : parts) {
            var s = part.trim();
            if(s.contains("i")){
                s = s.replaceAll("i","");
                imaginary = Double.parseDouble(s);
            }
            else{
                real = Double.parseDouble(s);
            }
        }
        if (real == null) {
            return String.format("0 + %fi", imaginary);
        }
        else if (imaginary == null) {
            return real.toString();
        }
        else { return String.format("%f + %fi", real, imaginary); }
    }


    public boolean tryParseToArray(String source, Complex[] res) {
        try{
            source = tryMathExpToComplex(source);
        }
        catch (NumberFormatException ignore){
            return false;
        }
        ParsePosition parsePosition = new ParsePosition(0);
        Complex result = parse(source, parsePosition);
        if (res.length < 1) {
            throw new RuntimeException("res length should >= 1");
        }
        var isComplex = parsePosition.getIndex() != 0;
        if(isComplex){
            res[0] = result;
        }
        return isComplex;
    }
}
