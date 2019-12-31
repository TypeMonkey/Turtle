package jg.cs.runtime.values;

import jg.cs.common.types.Type;

public class VoidValue extends Value<Void>{
  
  public static final VoidValue VOID = new VoidValue();

  private VoidValue() {
    super(null, Type.VOID);
  }

  @Override
  public String toString() {
    return "void";
  }

  
}
