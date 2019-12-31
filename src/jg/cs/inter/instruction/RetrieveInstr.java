package jg.cs.inter.instruction;

/**
 * Pushes a value of a struct's member onto the stack
 * 
 * The top-most value on the operand-stack is treated as the location of the struct instance
 * @author Jose
 *
 */
public class RetrieveInstr extends Instr{

  private final long offset;
  
  public RetrieveInstr(long offset, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.offset = offset;
  }

  public long getOffset() {
    return offset;
  }

  @Override
  public String toString() {
    return "retr:"+offset;
  }

}
