package jg.cs.inter.instruction;

/**
 * Changes the value of a struct's member
 * 
 * The topmost value on the operand stack is treated as the struct's address.
 * The following value is treated as the new value
 * 
 * The struct's address is popped back onto the operand stack
 * @author Jose
 *
 */
public class MutateInstr extends Instr{

  private final long offset;
  
  public MutateInstr(long offset, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.offset = offset;
  }

  public long getOffset() {
    return offset;
  }

  @Override
  public String toString() {
    return "mutate:"+offset;
  }

}
