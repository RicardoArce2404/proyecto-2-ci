import java.util.ArrayList;
import java_cup.runtime.Symbol;

public class Tac {
  private ArrayList<String> instructions = new ArrayList<>();

  private int temp_counter = 1;

  public Tac() {
  }

  public String newTemp() {
    return "t" + temp_counter++;
  }

  public void resetCounter() {
    temp_counter = 1;
  }

  public String primary(Object value) {
    String i;
    String t = newTemp();
    if (value instanceof String) {
      i = String.format("%s = %s", t, value);
    } else {
      i = String.format("%s = %s", t, ((Symbol)value).toString());
    }
    instructions.add(i);
    return t;
  }

  public String inc(String id) {
    // Note: This is a postorder increment, so the original value is the one
    // returned to be used in the expression.
    String original = newTemp();
    String increment = newTemp(); // Just to hold the 1 used in the sum.
    String incremented = newTemp();
    instructions.add(String.format("%s = %s", original, id));
    instructions.add(String.format("%s = %s", increment, "1"));
    instructions.add(String.format("%s = %s + %s", incremented, original, increment));
    instructions.add(String.format("%s = %s", id, incremented));
    return original;
  }

  public String dec(String id) {
    // Note: This is a postorder decrement, so the original value is the one
    // returned to be used in the expression.
    String original = newTemp();
    String decrement = newTemp(); // Just to hold the 1 used in the subtraction.
    String decremented = newTemp();
    instructions.add(String.format("%s = %s", original, id));
    instructions.add(String.format("%s = %s", decrement, "1"));
    instructions.add(String.format("%s = %s - %s", decremented, original, decrement));
    instructions.add(String.format("%s = %s", id, decremented));
    return original;
  }

  public void dump() {
    for (String i : instructions) {
      System.out.println(i);
    }
  }

}
