package jg.cs.runtime.errors;

import jg.cs.compile.nodes.atoms.BinaryOperator;

public class OverflowException extends ExecException{
  
  public OverflowException(BinaryOperator operator, String fileName) {
    super("Overflow Error: Operation with "+operator.getLeadToken().getImage()
         +" overflowed", operator.getLeadToken(), fileName);
  }
  
}
