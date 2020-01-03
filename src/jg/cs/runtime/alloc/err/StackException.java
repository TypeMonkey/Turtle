package jg.cs.runtime.alloc.err;

import java.util.EmptyStackException;

import jg.cs.runtime.alloc.OperandStack;

public class StackException extends RuntimeException{
  
  public enum ExceptionCode{   
    
    /**
     * Stack can no longer grow
     */
    NO_GROW,  
    
    /**
     * If there was an io reading/writing from the stack
     */
    IO_ERROR,
    
    /**
     * Stack is empty and a pop operation was attempted
     */
    EMPTY_STACK;
  }

  public StackException(ExceptionCode code) {
    super("Stack Error: "+code+" . ");
  }
}
