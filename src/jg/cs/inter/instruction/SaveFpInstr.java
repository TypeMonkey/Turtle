package jg.cs.inter.instruction;

public class SaveFpInstr extends Instr{

  private final long offset;
  
  public SaveFpInstr(long offset, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.offset = offset;
  }

  public long getOffset() {
    return offset;
  }

  @Override
  public String toString() {
    return "fpsave:"+offset;
  }

  
}
