import java.io.*;
import java_cup.runtime.Symbol;

public class Main {
  // Receives the name of the source code file as the first parameter.
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Please enter filename as first parameter.");
      return;
    }

    FileReader r = new FileReader("../test/" + args[0]);
    Lexer l = new Lexer(r);
    parser p = new parser(l);
    p.debug_parse();
  }
}
