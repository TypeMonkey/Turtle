package jg.cs.inter.instruction;

/**
 * Instructions that don't have a pre-written argument (and gather arguments from the stack directly)
 * @author Jose
 *
 */
public class NoArgInstr extends Instr{
  
  /**
   * Instructions that don't have a pre-written argument
   * @author Jose
   *
   */
  public enum NAInstr{   
    /**
     * Adds two integers from the stack
     */
    IADD,
    
    /**
     * Adds two Strings from the stack
     */
    SADD, 
    
    /**
     * Subtracts two integers from the stack
     */
    ISUB,
    
    /**
     * Multiplies two integers from the stack
     */
    IMUL,
    
    /**
     * Checks whether the top-most integer 
     * is less than the integer below it 
     * on the stack
     */
    ILESS,
    
    /**
     * Checks whether the top-most integer 
     * is greater than the integer below it 
     * on the stack
     */
    IGREAT,
    
    /**
     * Checks whether the top-most integer 
     * is less than or equal to the integer below it 
     * on the stack
     */
    ILESSQ,
    
    /**
     * Checks whether the top-most integer 
     * is greater than or equal to the integer below it 
     * on the stack
     */
    IGREATQ,
    
    /**
     * Checks whether the two top-most values on the stack
     * are equal
     */
    EQUAL,
    
    /**
     * Checks whether the top-most integer 
     * is less than the integer below it 
     * on the stack
     */
    NOTEQ,
    
    /**
     * Returns to caller's frame
     */
    RET;
  }

  private final NAInstr instr;
  
  public NoArgInstr(NAInstr instr, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.instr = instr;
  }
  
  public NAInstr getInstr() {
    return instr;
  }

  @Override
  public String toString() {
    return instr.toString().toLowerCase();
  }

}
