package jg.cs.inter.instruction;

import jg.cs.common.types.Type;

/**
 * Instruction that signifies a section of code that is under a label
 * @author Jose
 *
 */
public class LabelInstr extends Instr{
  
  private final String labelName;
  private final int riCode;
  
  public LabelInstr(String labelName, int riCode, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.labelName = labelName;
    this.riCode = riCode;
  }
  
  public String getLabelName() {
    return labelName;
  }

  public boolean isBuiltin() {
    return riCode != -1;
  }

  public String getLabel() {
    return labelName;
  }
  
  public int getRiCode() {
    return riCode;
  }
  
  @Override
  public String toString() {
    return (isBuiltin() ? "("+riCode+")" : "")+labelName+":~~~~~";
  }
  
}
