package jg.cs.common;

import java.util.Arrays;
import java.util.Objects;

import jg.cs.common.types.Type;

public class FunctionIdentity {
  
  private final FunctionSignature signature;
  private final Type returnType;
  
  public FunctionIdentity(FunctionSignature signature, Type returnType) {
    this.signature = signature;
    this.returnType = returnType;
  }

  public FunctionSignature getSignature() {
    return signature;
  }

  public Type getReturnType() {
    return returnType;
  }
  
  public boolean equals(Object object) {
    if (object instanceof FunctionIdentity) {
      FunctionIdentity other = (FunctionIdentity) object;
      return other.getSignature().equals(signature) && 
             other.returnType == returnType;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(signature, returnType);
  }
  
  public String toString() {    
    return signature + " >>>> "+returnType;
  }
}
