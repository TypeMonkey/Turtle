package jg.cs.compile.errors;

import jg.cs.compile.nodes.DataDeclaration;
import jg.cs.compile.nodes.FunctDefExpr;

public class MisplacedException extends RuntimeException{
  
  public MisplacedException(FunctDefExpr culritFunc, String fileName) {
    super("Error: Cannot return a function at <ln: "+culritFunc.getLeadLnNumber()
          +" ,col: "+culritFunc.getLeadColNumber()+", "+fileName+">");
  }
  
  public MisplacedException(DataDeclaration declaration, String fileName) {
    super("Error: Struct declarations must be top-level. "
        + "The struct '"+declaration.getName().getImage()+"' isn't top-level." 
        + "at <ln: "+declaration.getLeadLnNumber()
        + ",col: "+declaration.getLeadColNumber()
        + ", "+fileName+">");
  }
}
