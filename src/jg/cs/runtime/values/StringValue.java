package jg.cs.runtime.values;

import jg.cs.common.types.Type;

public class StringValue extends Value<String>{

  public StringValue(String actualValue) {
    super(actualValue, Type.STRING);
  }

  @Override
  public String toString() {
    return actualValue;
  }

}
