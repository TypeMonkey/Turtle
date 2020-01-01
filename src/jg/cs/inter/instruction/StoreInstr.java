package jg.cs.inter.instruction;

import jg.cs.common.types.Type;

/**
 * Stores the topmost value of the operand stack onto a space in the current stack frame
 * 
 * The topmost value is popped from the operand stack
 * @author Jose
 *
 */
public class StoreInstr extends Instr{
  private final long index;
  
  public StoreInstr(long index, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.index = index;
  }

  public long getIndex() {
    return index;
  }
  
  @Override
  public String toString() {
    return "store:"+index;
  }
}
