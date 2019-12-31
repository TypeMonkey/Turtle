package jg.cs.inter.instruction;

/**
 * Represents an Intermediate Representation Instruction for the Turtle language
 * 
 * Turtle Runtime Architecture:
 * -Turtle Programs are compiled to IR instructions which are then executed by 
 *  the Turtle Runtime Environment (TRE)
 *  
 * - During runtime, there are two main stacks: the operand and function stack.
 * 
 * The operand stack is used to temporary store instruction operands.
 * Example: the IADD instruction pops two integers from the operand stack
 *          the load/store instructions push and pop instructions respectively to/from the stack        
 * 
 * @author Jose
 *
 */
public abstract class Instr {
  
  private final int lineNumber;
  private final int colNumber;
  
  /**
   * Constructs an Instruction with debug location
   * @param relativeLine - the line from which this instruction was generated from
   * @param relativeColumn - the column from which this instruction was generated from
   */
  public Instr(int relativeLine, int relativeColumn){
    this.lineNumber = relativeLine;
    this.colNumber = relativeColumn;
  }
  
  public int getLineNumber() {
    return lineNumber;
  }

  public int getColNumber() {
    return colNumber;
  }
  
  public abstract String toString();
}
