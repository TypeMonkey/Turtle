package jg.cs.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jg.cs.common.types.Type;;
/**
 * This class holds all the FunctionIdentities that are
 * provided as "built-in" function in sNEK such as the following:
 *  - input : -> void >>>> string
 *  - print : -> string >>> string
 *  - println : -> string >>> string
 *  - inc : -> int >>> int
 *  - dec : -> int >>> int
 *  - isInt : -> (int or boo or str) >>> bool
 *  - isBool : ->  (int or bool or str) >>> bool
 *  - isStr : ->  (int or bool or str) >>> bool
 *  - type : -> (int or boolor str) >>> str (returns the type name as a string)
 *  - len : -> string >>> int (returns size of string)
 * @author Jose
 *
 */
public enum BuiltInFunctions implements FunctionLike{
    
  /**
   * Blocks for an input string on standard input. Returns input as a string
   */
  INPUT(new FunctionIdentity(new FunctionSignature("input", Type.VOID), Type.STRING),0),
  
  /**
   * Prints a string to stdout, without a new line at the end
   */
  PRINT(new FunctionIdentity(new FunctionSignature("print", Type.STRING), Type.STRING),1),
  
  /**
   * Prints a string to stdout, with a new line at the end
   */
  PRINTLN(new FunctionIdentity(new FunctionSignature("println", Type.STRING), Type.STRING),2),
  
  /**
   * Returns an integer with incremented by one
   */
  INC(new FunctionIdentity(new FunctionSignature("inc", Type.INTEGER), Type.INTEGER),3),
  
  /**
   * Returns an integer with decremented by one
   */
  DEC(new FunctionIdentity(new FunctionSignature("dec", Type.INTEGER), Type.INTEGER),4),
  
  /**
   * Returns the size of a string
   */
  LEN(new FunctionIdentity(new FunctionSignature("len", Type.STRING), Type.INTEGER),5),
  
  /**
   * Returns the string representation of an integer
   */
  TO_STR_I(new FunctionIdentity(new FunctionSignature("toStr", Type.INTEGER), Type.STRING),6),
  
  /**
   * Returns the string representation of a boolean
   */
  TO_STR_B(new FunctionIdentity(new FunctionSignature("toStr", Type.BOOLEAN), Type.STRING),7);
  
  private final FunctionIdentity identity;
  private final int irCode; 
  
  public static final Map<FunctionSignature, FunctionLike> BUILT_IN_MAP;
  static {
    HashMap<FunctionSignature, BuiltInFunctions> temp = new HashMap<>();
    
    for (BuiltInFunctions funcs : BuiltInFunctions.values()) {
      temp.put(funcs.identity.getSignature(), funcs);
    }
    
    BUILT_IN_MAP = Collections.unmodifiableMap(temp);
  }

  
  private BuiltInFunctions(FunctionIdentity identity, int irCode) {
    this.identity = identity;
    this.irCode = irCode;
  }
  
  /**
   * Used during the compilation and execution of IR Code
   * to quickly identify which built-in function is which.
   * @return this BuiltInFunction's IR Code
   */
  public int getIrCode() {
    return irCode;
  }
  
  public FunctionIdentity getIdentity() {
    return identity;
  }

  @Override
  public boolean isBuiltIn() {
    return true;
  }
}
