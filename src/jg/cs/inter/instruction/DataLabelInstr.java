package jg.cs.inter.instruction;

public class DataLabelInstr extends Instr{

  private final String typeName;
  private final int [] typeCode;
  
  public DataLabelInstr(String typeName, int [] typeCode, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.typeName = typeName;
    this.typeCode = typeCode;
  }

  public String getTypeName() {
    return typeName;
  }

  public int[] getTypeCode() {
    return typeCode;
  }

  @Override
  public String toString() {
    String x = "-"+typeName+":";
    
    for (int i = 0; i < typeCode.length; i++) {
      if (i == typeCode.length - 1) {
        x += typeCode[i];
      }
      else {
        x += typeCode[i] +",";
      }
    }
    
    return x;
  }
}
