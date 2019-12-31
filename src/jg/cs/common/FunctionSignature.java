package jg.cs.common;

import java.util.Arrays;
import java.util.Objects;

import jg.cs.common.types.Type;

public class FunctionSignature {
  
  private final String name;
  private final Type [] parameterTypes;
  
  public FunctionSignature(String name, Type ... parameterTypes) {
    this.name = name;
    this.parameterTypes = parameterTypes;
  }

  public String getName() {
    return name;
  }

  public Type[] getParameterTypes() {
    return parameterTypes;
  }
  
  public boolean equals(Object object) {
    if (object instanceof FunctionSignature) {
      FunctionSignature other = (FunctionSignature) object;
            
      if (!other.getName().equals(name)) {
        return false;
      }
      
      if (parameterTypes.length == other.parameterTypes.length) {
        for (int i = 0; i < parameterTypes.length; i++) {
          if (!parameterTypes[i].equals(other.parameterTypes[i])) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, Arrays.deepHashCode(parameterTypes));
  }
  
  public String toString() {
    String str = name+" : ";
    
    for(int i = 0; i < parameterTypes.length; i++) {
      if (i == parameterTypes.length - 1) {
        str += parameterTypes[i];
      }
      else {
        str += parameterTypes[i]+" -> ";
      }
    }
    
    return str;
  }
}
