package jg.cs.runtime.alloc;

import java.util.NoSuchElementException;

/**
 * The function stack keeps track of function frames.
 * 
 * Each function frames houses values of a function's local variables
 * and arguments
 * 
 * @author Jose
 *
 */
public interface FunctionStack {

  /**
   * Registers a new frame on to the function stack
   * 
   * @param insIndex - the current instruction index
   */
  public void changeFPBy(long insIndex);
  
  /**
   * Retrieves a value from the current frame at a given offset
   * @param offset - the offset, with respect to the current frame
   * @return the value
   * 
   * @throws IllegalArgumentException - if the offset is invalid
   */
  public long retrieveAtOffset(long offset) throws IllegalArgumentException;
  
  /**
   * Saves a value to the given offset on the current frame
   * @param offset - the offset, with respect to the current frame
   * @param value - the value to save
   * @throws IllegalArgumentException - if the offset is invalid
   */
  public void saveAtOffset(long offset, long value) throws IllegalArgumentException;
  
  /**
   * Gets the total values stored onto the stack
   * @return the total values stored onto the stack
   */
  public long getTotalElements();
  
  /**
   * Returns the current frame pointer
   * @return the current frame pointer
   */
  public int getCurrentFP();
  
  /**
   * Gets the number of frames registered on the stack
   * @return the number of frames registered on the stack
   */
  public long getTotalFrames();
}
