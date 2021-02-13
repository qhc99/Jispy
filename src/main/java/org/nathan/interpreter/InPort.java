package org.nathan.interpreter;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class InPort {
    BufferedReader file;
    String line = "";
    String tokenizer = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)";

    public InPort(InputStream in) {
        file = new BufferedReader(new InputStreamReader(in));
    }

    public InPort(String in){
        file = new BufferedReader(new StringReader(in));
    }

    public Object nextToken() {
        while (true) {
            if (line.equals("")) {
                try {
                    line = file.readLine();
                    if(line == null) line = "";
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    continue;
                }
            }
            if (line.equals("")) {
                return Symbol.eof;
            }
            var pattern = Pattern.compile(tokenizer);
            var matcher = pattern.matcher(line);
            if(matcher.find()){
                var s = matcher.start(1);
                var e = matcher.end(1);
                var token = line.substring(s,e);
                line = line.substring(e);
                return token;
            }
        }
    }
}
