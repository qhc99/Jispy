package org.nathan.interpreter.LiteralParser;

//Regex://Digits:(\d[\d_]+\d|\d)
//ExponentPart: ([eE][+-]?(\d[\d_]+\d|\d))
//FloatTypeSuffix: ([fFdD])
//Double Type1: ([+-]?(\d[\d_]+\d|\d)\.(\d[\d_]+\d|\d)?([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)
//Double Type2: ([+-]\.(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)
//Double Type3: ([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))([fFdD])?)
//Double Type4: ([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD]))
//All Double: (([+-]?(\d[\d_]+\d|\d)\.(\d[\d_]+\d|\d)?([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)|([+-]\.(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])?)|([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))([fFdD])?)|([+-]?(\d[\d_]+\d|\d)([eE][+-]?(\d[\d_]+\d|\d))?([fFdD])))
public class FloatingPointLiteralsParser {
    private final String source;
    private int relyIndex;

    public FloatingPointLiteralsParser(String s) {
        source = s;
    }
}
