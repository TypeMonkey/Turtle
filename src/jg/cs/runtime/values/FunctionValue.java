package jg.cs.runtime.values;

import jg.cs.compile.nodes.FunctDefExpr;

public class FunctionValue extends Value<FunctDefExpr>{

  public FunctionValue(FunctDefExpr actualValue) {
    super(actualValue, actualValue.getReturnType().getActualValue());
  }

  @Override
  public String toString() {
    return "function: "+actualValue.getIdentity();
  }

}
