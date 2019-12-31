package jg.cs.compile.nodes.atoms;

import jg.cs.common.OperatorKind;
import net.percederberg.grammatica.parser.Token;

public class BinaryOperator extends Atom<OperatorKind>{
  
  public BinaryOperator(Token operator) {
    super(operator, stringToOp(operator.getImage()));
  }
  
  @Override
  public String toString() {
    return "OP ~ "+getActualValue();
  }
  
  public static OperatorKind stringToOp(String op) {
    switch (op) {
    case "+":
      return OperatorKind.PLUS;
    case "-":
      return OperatorKind.MINUS;
    case "*":
      return OperatorKind.TIMES;
    case "<":
      return OperatorKind.LESS;
    case ">":
      return OperatorKind.GREAT;
    case "=":
      return OperatorKind.EQUAL;
    case "^":
      return OperatorKind.EXP;
    case "<=":
      return OperatorKind.LESSQ;
    case ">=":
      return OperatorKind.GREATQ;
    case "!=":
      return OperatorKind.NOTEQUAL;
    default:
      return null;
    }
  }
  

}
