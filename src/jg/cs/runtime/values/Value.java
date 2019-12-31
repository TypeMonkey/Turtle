package jg.cs.runtime.values;

import jg.cs.common.types.Type;

public abstract class Value<T> {

  protected final T actualValue;
  protected final Type valueType;
  
  public Value(T actualValue, Type valueType) {
    this.actualValue = actualValue;
    this.valueType = valueType;
  }
  
  public abstract String toString();

  public T getActualValue() {
    return actualValue;
  }

  public Type getValueType() {
    return valueType;
  } 
}
