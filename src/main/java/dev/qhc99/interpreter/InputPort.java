package dev.qhc99.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Pattern;

import static dev.qhc99.interpreter.Symbol.eof;

class InputPort implements Closeable{
  final BufferedReader reader;
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
    String line_cache = "";
    while(true) {
      if(!queue.isEmpty()){
        return queue.poll();
      }
      else if(line_cache.equals("")){
        try{
          line_cache = reader.readLine();
        }
        catch(IOException e){
          e.printStackTrace(System.err);
          throw new RuntimeException(e);
        }
      }

      if(line_cache == null){
        return eof;
      }
      else if(line_cache.equals("")){ continue; }

      var matcher = pattern.matcher(line_cache);
      int idx = 0;
      while(matcher.find(idx)) {
        var s = matcher.start(1);
        var e = matcher.end(1);
        if(s == e){break;}
        var token = line_cache.substring(s, e);
        idx = e;
        if(!token.startsWith(";")){
          queue.add(token);
        }
      }
      line_cache = "";
    }
  }

  @Override
  public void close() throws IOException{
    reader.close();
  }
}
