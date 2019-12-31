package jg.cs.compile.errors;

import jg.cs.compile.nodes.DataDeclaration;

public class DuplicateDataDeclaration extends RuntimeException{
  
  public DuplicateDataDeclaration(DataDeclaration culpritDec, String fileName) {
    super("Error: The struct '"+culpritDec.getName().getImage()+"' "
        + "has already been declared. "
        + "<ln: "+culpritDec.getLeadLnNumber()+", "
        + "col: "+culpritDec.getLeadColNumber()+","
        + "file: "+fileName+">");
  }
  
}
