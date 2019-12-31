package jg.cs.common;

/**
 * Describes expressions that can be identified with a FunctionIdentity
 * @author Jose Guaro
 *
 */
public interface FunctionLike {
  
  
  
  /**
   * Returns the FunctionIdentity
   * @return the FunctionIdentity
   */
  FunctionIdentity getIdentity();
  
  /**
   * Checks whether this function is built-in
   * 
   * @return true if this function is built-in, or false if not
   */
  boolean isBuiltIn();
}
