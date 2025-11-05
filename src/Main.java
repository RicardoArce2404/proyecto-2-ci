import java.io.*;
import java_cup.runtime.ComplexSymbolFactory;

public class Main {
  // Receives the name of the source code file as the first parameter.
  public static void main(String[] args) throws Exception {
    if (args[0].length() == 0) {
      System.out.println("Please enter filename as first parameter.");
      return;
    }

    FileInputStream f = new FileInputStream("../test/" + args[0]);
    ComplexSymbolFactory fact = new ComplexSymbolFactory();
    parser p = new parser(new Lexer(f, fact), fact);
    p.parse();
  }
}
