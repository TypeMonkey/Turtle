package jg.cs.inter.instruction;

import jg.cs.common.types.Type;

/**
 * Instruction that signifies a section of code that is under a label
 * @author Jose
 *
 */
public class LabelInstr extends Instr{
  
  private final String labelName;
  
  public LabelInstr(String labelName, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.labelName = labelName;
  }
  
  @Override
  public String toString() {
    return labelName+":~~~~~";
  }
  
}
