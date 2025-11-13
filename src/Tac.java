import java.util.ArrayList;
import java_cup.runtime.Symbol;

public class Tac {
  private ArrayList<String> data = new ArrayList<>();
  private ArrayList<String> code = new ArrayList<>();

  private int tmp_counter = 1;
  private int lbl_counter = 1;
  private int arr_counter = 1;

  private ArrayList<Integer> int_arr_buffer = new ArrayList<>();
  private ArrayList<Integer> char_arr_buffer = new ArrayList<>();

  public String newTemp() {
    return "t" + tmp_counter++;
  }

  public String newLbl() {
    return "L" + lbl_counter++ + ":";
  }

  public String newArr() {
    return "arr" + arr_counter++ + ":";
  }

  // public void resetTmp() {
  //   tmp_counter = 1;
  // }
  //
  // public void resetLbl() {
  //   lbl_counter = 1;
  // }
  //
  // public void resetIntArr() {
  //   int_arr_buffer.clear();
  // }
  //
  // public void resetCharArr() {
  //   char_arr_buffer.clear();
  // }

  public String primary(Object value) {
    String i;
    String t = newTemp();
    if (value instanceof String) {
      i = String.format("%s = %s", t, value);
    } else {
      i = String.format("%s = %s", t, ((Symbol) value).toString());
    }
    code.add(i);
    return t;
  }

  public String inc(String id) {
    // Note: This is a postorder increment, so the original value is the one
    // returned to be used in the expression.
    String original = newTemp();
    String increment = newTemp(); // Just to hold the 1 used in the sum.
    String incremented = newTemp();
    code.add(String.format("%s = %s", original, id));
    code.add(String.format("%s = 1", increment));
    code.add(String.format("%s = %s + %s", incremented, original, increment));
    code.add(String.format("%s = %s", id, incremented));
    return original;
  }

  public String dec(String id) {
    // Note: This is a postorder decrement, so the original value is the one
    // returned to be used in the expression.
    String original = newTemp();
    String decrement = newTemp(); // Just to hold the 1 used in the subtraction.
    String decremented = newTemp();
    code.add(String.format("%s = %s", original, id));
    code.add(String.format("%s = 1", decrement));
    code.add(String.format("%s = %s - %s", decremented, original, decrement));
    code.add(String.format("%s = %s", id, decremented));
    return original;
  }

  public String power(String base, String exp) {
    String t = newTemp();
    code.add(String.format("param %s", base));
    code.add(String.format("param %s", exp));
    code.add(String.format("%s = call pow, 2", t));
    return t;
  }

  // type: int or float.
  public String minus(String value, String type) {
    String t_val = newTemp();
    String minus_one = newTemp();
    String t_res = newTemp();
    code.add(String.format("%s = %s", t_val, value));
    code.add(String.format("%s = -1", minus_one));
    switch (type) {
      case "int":
        code.add(String.format("%s = %s *_i %s", t_res, minus_one, t_val));
        break;
      case "float":
        code.add(String.format("%s = %s *_f %s", t_res, minus_one, t_val));
        break;
      default:
        System.out.println("Unrecognized operand type in Tac.minus().");
        break;
    }
    return t_res;
  }

  public String neg(String value) {
    // The behavior of negation is: If it's true, return false; if it's
    // false, return true. Because booleans here are just 0 or 1, the
    // desired behavior is: 0 becomes 1; 1 becomes 0. That, seen as
    // points in a Cartesian plane, gives the points (0, 1) and (1, 0).
    // Applying what we learned from Wynta in general maths course,
    // the linear function that goes through both points is
    //                        y = 1 - x,
    // so we are going to use it to implement the negation. Thanks, Wynta.
    String t_val = newTemp();
    String one = newTemp();
    String t_res = newTemp();
    code.add(String.format("%s = %s", t_val, value));
    code.add(String.format("%s = 1", one));
    code.add(String.format("%s = %s -_i %s", t_res, one, t_val));
    return t_res;
  }

  // op: *, div, intdiv, mod.
  public String multExpr(String s, String t, String op, String type) {
    String d = newTemp();
    String op_type = (type == "int") ? "i" : "f";
    switch (op) {
      case "*":
        code.add(String.format("%s = %s *_%s %s", d, s, op_type, t));
        break;
      case "div":
        code.add(String.format("%s = %sdiv %s %s", d, op_type, s, t));
        break;
      case "intdiv":
        code.add(String.format("%s = %sintdiv %s %s", d, op_type, s, t));
        break;
      case "mod":
        code.add(String.format("%s = %smod %s %s", d, op_type, s, t));
        break;
      default:
      System.out.println("Unrecognized operation in Tac.multOp().");
        break;
    }
    return d;
  }

  // op: +, -.
  public String aditExpr(String s, String t, String op, String type) {
    String d = newTemp();
    String op_type = (type == "int") ? "i" : "f";
    switch (op) {
      case "+":
        code.add(String.format("%s = %s +_%s %s", d, s, op_type, t));
        break;
      case "-":
        code.add(String.format("%s = %s -_%s %s", d, s, op_type, t));
        break;
      default:
      System.out.println("Unrecognized operation in Tac.aditOp().");
        break;
    }
    return d;
  }

  // op: >, >=, <, <=, ==, !=.
  public String relExpr(String s, String t, String op) {
    String d = newTemp();
    code.add(String.format("%s = %s %s %s", d, s, op, t));
    return d;
  }

  public String andExpr(String s, String t) {
    String d = newTemp();
    // An AND operation is almost equivalent to a multiplication, given that
    // booleans are 0 or 1.
    code.add(String.format("%s = %s *_i %s", d, s, t));
    return d;
  }

  public String orExpr(String s, String t) {
    String d = newTemp();
    String zero = newTemp();
    String res = newTemp();
    // An OR operation is almost equivalent to an addition, given that
    // booleans are 0 or 1. Just we need to restrict the 1 + 1 to be still 1.
    // The restriction can be done using "d > 0", which can only be 0 or 1.
    code.add(String.format("%s = %s +_i %s", d, s, t));
    code.add(String.format("%s = 0", zero));
    code.add(String.format("%s = %s > %s", res, d, zero));
    return res;
  }

  public void assignExpr(String id, String expr) {
    code.add(id + " = " + expr);
  }

  // public void assignArr(String id, String type) {
  //   if (type == "int") {
  //     String 
  //     data.add(String.format("%s: .word ", ))
  //   } else {
  //     data.add(String.format("%s: .", args))
  //   } 
  // }

  public void dump() {
    for (String i : code) {
      System.out.println(i);
    }
  }

}
