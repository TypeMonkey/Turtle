package jg.cs.runtime.alloc;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

/**
 * An operand stack is used by instructions as a 
 * sort of "workbench" to store temporary values.
 * @author Jose
 *
 */
public interface OperandStack {

  /**
   * Pushes a long - meant to be unsigned - on to the top
   * of the operand stack
   * 
   * Note: All values in Turtle are 64-bit, with the left most bit reserved
   *       as a tagged bit
   * 
   * @param value - the value to push
   */
  void pushOperand(long value);
  
  /**
   * Pops an operand from the top of the stack
   * @return the topmost operand
   * @throws EmptyStackException - there are no more elements on the stack
   */
  long popOperand() throws EmptyStackException;
  
  /**
   * Returns the amount of operands currently on the stack
   * @return the amount of operands currently on the stack
   */
  int stackSize();
  
  /**
   * Clears the operand stack of elements
   */
  void clear();
  
  /**
   * Returns the string representation of the stack
   * @return the string representation of the stack
   */
  String toString();
}
