package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Pattern;

import static org.nathan.interpreter.Symbol.eof;

class InputPort implements Closeable{
  final BufferedReader reader;
  String line = "";
  final Queue<String> queue = new ArrayDeque<>();
  static final String tokenizer = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)";
  static final Pattern pattern = Pattern.compile(tokenizer);


  InputPort(@NotNull InputStream in){
    reader = new BufferedReader(new InputStreamReader(in));
  }

  InputPort(@NotNull String in){
    reader = new BufferedReader(new StringReader(in));
  }

  InputPort(@NotNull File file){
    try{
      this.reader = new BufferedReader(new FileReader(file));
    }
    catch(FileNotFoundException e){
      throw new RuntimeException(e);
    }
  }

  /**
   * @return string or Symbol
   */
  Object nextToken(){
    while(true) {
      if(!queue.isEmpty()){
        return queue.poll();
      }
      else if(line.equals("")){
        try{
          line = reader.readLine();
        }
        catch(IOException e){
          e.printStackTrace(System.err);
          throw new RuntimeException(e);
        }
      }

      if(line == null){
        return eof;
      }
      else if(line.equals("")){ continue; }

      var matcher = pattern.matcher(line);
      int idx = 0;
      while(matcher.find(idx)) {
        var s = matcher.start(1);
        var e = matcher.end(1);
        if(s == e){break;}
        var token = line.substring(s, e);
        idx = e;
        if(!token.startsWith(";")){
          queue.add(token);
        }
      }
      line = "";
    }
  }

  @Override
  public void close() throws IOException{
    reader.close();
  }
}
