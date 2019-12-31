package jg.cs.runtime.errors;

import net.percederberg.grammatica.parser.Token;

public class ExecException extends RuntimeException{

  public ExecException(String message, Token nearestLocation, String fileName) {
    super(message+" , at <ln: "+nearestLocation.getStartLine()
          +" , col: "+nearestLocation.getStartColumn()
          +",  "+fileName+">");
  }
  
}
