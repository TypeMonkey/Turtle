package jg.cs.common.types;

import jg.cs.common.FunctionIdentity;

public class FunctionType extends Type{

  public FunctionType(FunctionIdentity identity) {
    super(formString(identity));
  }

  private static String formString(FunctionIdentity identity) {
    String str = "";
    
    Type [] parameterTypes = identity.getSignature().getParameterTypes();
    for(int i = 0; i < parameterTypes.length; i++) {
      if (i == parameterTypes.length - 1) {
        str += parameterTypes[i];
      }
      else {
        str += parameterTypes[i]+" -> ";
      }
    }
    
    return str + " => "+identity.getReturnType();
  }

}
