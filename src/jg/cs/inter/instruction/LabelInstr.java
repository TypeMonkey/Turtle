package jg.cs.inter.instruction;

import jg.cs.common.types.Type;

/**
 * Instruction that signifies a section of code that is under a label
 * @author Jose
 *
 */
public class LabelInstr extends Instr{
  
  private final String labelName;
  private final int argAmount;
  
  public LabelInstr(String labelName, int relativeLine, int relativeCol) {
    this(labelName, 0, relativeLine, relativeCol);
  }
  
  public LabelInstr(String labelName, int argAmount, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.labelName = labelName;
    this.argAmount = argAmount;
  }
  
  public String getLabel() {
    return labelName;
  }
  
  public int getArgAmount() {
    return argAmount;
  }
  
  @Override
  public String toString() {
    return labelName+":"+(argAmount == 0 ? "" : argAmount)+"~~~~~";
  }
  
}
