package jg.cs.inter.instruction;

/**
 * Jumps instructions
 * @author Jose
 *
 */
public class JumpInstr extends Instr{
  
  /**
   * Instructions with the prefix "jmp" create no new stack frames
   * 
   * Call instructions creates a new stack frame
   * @author Jose
   *
   */
  public enum Jump{
    /**
     * Jump if top-most value is true
     */
    JMPT,
    
    /**
     * Jump if top-most value is false
     */
    JMPN,
    
    /**
     * Jump to label unconditionally
     */
    JMP,
    
    /**
     * Call a label
     */
    CALL; 
  }
  
  private final Jump jump;
  private final String location;
  
  public JumpInstr(Jump jump, String label, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.jump = jump;
    this.location = label;
  }
  
  public Jump getJump() {
    return jump;
  }

  public String getTargetLabel() {
    return location;
  }

  @Override
  public String toString() {
    return jump.toString().toLowerCase()+":"+location;
  }
  
  
}
