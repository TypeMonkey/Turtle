package jg.cs.compile.errors;

import jg.cs.common.FunctionSignature;
import net.percederberg.grammatica.parser.Token;

public class DuplicateFunctionException extends RuntimeException{

  public DuplicateFunctionException(Token culpritDefKey, 
                                    FunctionSignature culpritSignature,
                                    String fileName) {
    super(
        "Error: The function of signature '"+culpritSignature+"' has already been declared. "
      + "<ln: "+culpritDefKey.getStartLine()+
      ", col: "+culpritDefKey.getStartColumn()+
      ", file: "+fileName+">");
  }
  
}
