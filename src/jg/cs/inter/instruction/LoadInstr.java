package jg.cs.inter.instruction;

/**
 * Loads a value from the stack frame to the operand stack
 * @author Jose
 */
public class LoadInstr<T> extends Instr{
  
  private final LoadType type;
  private final T value;
  
  public enum LoadType{
    
    /**
     * Loads from address
     */
    MLOAD,
    
    /**
     * Loads from constant
     */
    ICLOAD,
    SCLOAD;
  }
  
  public LoadInstr(LoadType type, T value, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.type = type;
    this.value = value;
  }

  public T getValue() {
    return value;
  }
  
  public LoadType getType() {
    return type;
  }

  @Override
  public String toString() {
    return type.toString().toLowerCase()+":"+value;
  }

}
