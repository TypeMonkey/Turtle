package jg.cs.compile.errors;

import jg.cs.common.OperatorKind;
import jg.cs.common.types.Type;
import jg.cs.compile.nodes.BinaryOpExpr;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.nodes.FunctDefExpr;
import jg.cs.compile.nodes.IdenTypeValTuple;
import net.percederberg.grammatica.parser.Token;

public class TypeMismatchException extends RuntimeException{
  
  public TypeMismatchException(IdenTypeValTuple culpritVariable, Type expressionType, String fileName) {
    super("Error: The variable '"+culpritVariable.getIdentifier().getActualValue()+"' "
        + "is declared with type '"+culpritVariable.getType()+"' but is assigned "
        + "an expression of type '"+expressionType+"' at "
        +"<ln: "+culpritVariable.getIdentifier().getLeadLnNumber()+","
        +"col: "+culpritVariable.getIdentifier().getLeadColNumber()+", "
        +fileName+">");
  }
  
  public TypeMismatchException(Token leadToken, Type expected, Type got, String fileName) {
    super("Error: Expected type '"+expected+"' , but got type '"+got+"' "
         +"at <ln: "+leadToken.getStartLine()
         +" ,col: "+leadToken.getStartColumn()+", "+fileName+">");
  }
  
  public TypeMismatchException(OperatorKind op, Expr culpritExpr, Type got,  Type expected, String fileName) {
    super("Error: For the "+op+" , both operands must be of type "+expected
          +" but an operand is of type "+expected+" at <ln: "+culpritExpr.getLeadLnNumber()
          +",col: "+culpritExpr.getLeadColNumber()+", "+fileName+">");
  }
}
