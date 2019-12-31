package jg.cs.inter.instruction;

import jg.cs.common.types.Type;

/**
 * Stores the topmost value of the operand stack onto a space in the current stack frame
 * 
 * The topmost value is popped from the operand stack
 * @author Jose
 *
 */
public class StoreInstr extends Instr{
  private final StoreType type;
  private final long index;
  
  public enum StoreType{
    
    /**
     * Stores from address
     */
    ISTORE,
    SSTORE,
    RSTORE;
  }
  
  public StoreInstr(StoreType type, long index, int relativeLine, int relativeColumn) {
    super(relativeLine, relativeColumn);
    this.type = type;
    this.index = index;
  }

  public long getIndex() {
    return index;
  }
  
  public StoreType getType() {
    return type;
  }

  public boolean isConstant() {
    return false;
  }

  @Override
  public String toString() {
    return type.toString().toLowerCase()+":"+index;
  }

  public static StoreType properStoreType(Type type) {
    if (type.equals(Type.BOOLEAN) || type.equals(Type.INTEGER)) {
      return StoreType.ISTORE;
    }
    else if (type.equals(Type.STRING)) {
      return StoreType.SSTORE;
    }
    else {
      return StoreType.RSTORE;
    }
  }
}
