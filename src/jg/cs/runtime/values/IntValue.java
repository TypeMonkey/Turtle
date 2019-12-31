package jg.cs.runtime.values;

import jg.cs.common.types.Type;

public class IntValue extends Value<Long> {

  public IntValue(Long actualValue) {
    super(actualValue, Type.INTEGER);
  }

  @Override
  public String toString() {
    return actualValue.toString();
  }
}
