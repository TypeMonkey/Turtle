package jg.cs.inter.instruction;

/**
 * Increases or decrease the current frame pointer 
 * by the provided amount
 * @author Jose
 *
 */
public class IncfpInstr extends Instr{

  private final long amount;
  
  public IncfpInstr(long amount, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.amount = amount;
  }
  
  public long getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "changefp:"+amount;
  }

}
