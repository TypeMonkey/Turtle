package jg.cs.runtime.values;

import jg.cs.common.types.Type;

public class BoolValue extends Value<Boolean>{

  public BoolValue(Boolean actualValue) {
    super(actualValue, Type.BOOLEAN);
  }

  @Override
  public String toString() {
    return actualValue.toString();
  }

}
