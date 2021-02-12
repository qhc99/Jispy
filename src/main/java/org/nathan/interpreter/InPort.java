package org.nathan.interpreter;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class InPort {
    BufferedReader file;
    String line = "";
    Queue<String> queue = new LinkedList<>();

    public InPort(InputStream in) {
        file = new BufferedReader(new InputStreamReader(in));
    }

    public InPort(String in){
        file = new BufferedReader(new StringReader(in));
    }

    Object nextToken() {
        while (true) {
            if (!queue.isEmpty()) return queue.poll();
            if (line.equals("")) {
                try {
                    line = file.readLine();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
            if (line.equals("")) {
                return Symbol.eof;
            }
            String tokenizer = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)";
            var pattern = Pattern.compile(tokenizer);
            var matcher = pattern.matcher(line);
            line = "";
            while (matcher.find()) {
                var token = matcher.group();
                if (!token.equals("") && !token.startsWith(";")) {
                    queue.add(token);
                }
            }
            if (!queue.isEmpty()) return queue.poll();
        }
    }
}
