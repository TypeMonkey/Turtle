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
  INPUT(new FunctionIdentity(new FunctionSignature("input", Type.VOID), Type.STRING)),
  
  /**
   * Prints a string to stdout, without a new line at the end
   */
  PRINT(new FunctionIdentity(new FunctionSignature("print", Type.STRING), Type.STRING)),
  
  /**
   * Prints a string to stdout, with a new line at the end
   */
  PRINTLN(new FunctionIdentity(new FunctionSignature("println", Type.STRING), Type.STRING)),
  
  /**
   * Returns an integer with incremented by one
   */
  INC(new FunctionIdentity(new FunctionSignature("inc", Type.INTEGER), Type.INTEGER)),
  
  /**
   * Returns an integer with decremented by one
   */
  DEC(new FunctionIdentity(new FunctionSignature("dec", Type.INTEGER), Type.INTEGER)),
  
  /**
   * Returns the size of a string
   */
  LEN(new FunctionIdentity(new FunctionSignature("len", Type.STRING), Type.INTEGER)),
  
  /**
   * Returns the string representation of an integer
   */
  TO_STR_I(new FunctionIdentity(new FunctionSignature("toStr", Type.INTEGER), Type.STRING)),
  
  /**
   * Returns the string representation of a boolean
   */
  TO_STR_B(new FunctionIdentity(new FunctionSignature("toStr", Type.BOOLEAN), Type.STRING));
  
  private final FunctionIdentity identity;
  
  public static final Map<FunctionSignature, BuiltInFunctions> BUILT_IN_MAP;
  static {
    HashMap<FunctionSignature, BuiltInFunctions> temp = new HashMap<>();
    
    for (BuiltInFunctions funcs : BuiltInFunctions.values()) {
      temp.put(funcs.identity.getSignature(), funcs);
    }
    
    BUILT_IN_MAP = Collections.unmodifiableMap(temp);
  }

  
  private BuiltInFunctions(FunctionIdentity identity) {
    this.identity = identity;
  }
  
  public FunctionIdentity getIdentity() {
    return identity;
  }

  @Override
  public boolean isBuiltIn() {
    return true;
  }
}
