package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.regex.Pattern;

class InputPort implements Closeable {
    BufferedReader file;
    String line = "";
    String tokenizer = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)";

    InputPort(@NotNull InputStream in) {
        file = new BufferedReader(new InputStreamReader(in));
    }

    InputPort(@NotNull String in) {
        file = new BufferedReader(new StringReader(in));
    }

    InputPort(@NotNull File file) {
        try {
            this.file = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return string or Symbol eof
     */
    Object nextToken() {
        while (true) {
            if (line.equals("")) {
                try {
                    line = file.readLine();
                    if (line == null) { line = ""; }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    throw new RuntimeException(e);
                }
            }
            // TODO file next line is not eof
            if (line.equals("")) {
                return Symbol.eof;
            }
            var pattern = Pattern.compile(tokenizer);
            var matcher = pattern.matcher(line);
            if (matcher.find()) {
                var s = matcher.start(1);
                var e = matcher.end(1);
                var token = line.substring(s, e);
                line = line.substring(e);
                return token;
            }
        }
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
