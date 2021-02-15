package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import static org.nathan.interpreter.Symbol.*;

public class InputPort implements Closeable {
    final BufferedReader file;
    String line = "";
    final Queue<String> queue = new LinkedList<>();
    static final String tokenizer = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)";
    static final Pattern pattern = Pattern.compile(tokenizer);

    public InputPort(@NotNull InputStream in) {
        file = new BufferedReader(new InputStreamReader(in));
    }

    public InputPort(@NotNull String in) {
        file = new BufferedReader(new StringReader(in));
    }

    public InputPort(@NotNull File file) {
        try {
            this.file = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return string or Symbol
     */
    public Object nextToken() {
        while (true) {
            if (!queue.isEmpty()) {
                return queue.poll();
            }
            if (line.equals("")) {
                try {
                    line = file.readLine();
                    if (line == null) { return eof; }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    throw new RuntimeException(e);
                }
            }
            if (line.equals("")) {
                return new_line;
            }
            var matcher = pattern.matcher(line);
            int idx = 0;
            while (matcher.find(idx)) {
                var s = matcher.start(1);
                var e = matcher.end(1);
                if (s == e) {break;}
                var token = line.substring(s, e);
                idx = e;
                queue.add(token);
            }
            line = "";
        }
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
