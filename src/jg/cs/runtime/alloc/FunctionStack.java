package jg.cs.runtime.alloc;

import java.util.NoSuchElementException;

/**
 * The function stack keeps track of function frames.
 * 
 * Each function frames houses values of a function's local variables
 * and arguments
 * 
 * When creating a new frame/calling a function,
 * it's essential that registerFrame() is called
 * AFTER arguments have been placed on the stack
 * 
 * When exiting a frame/returing to a caller,
 * it's essential that exitFrame() is called 
 * @author Jose
 *
 */
public interface FunctionStack {

  /**
   * Registers a new frame on to the function stack
   */
  public void registerFrame();
  
  /**
   * Exits the current frame
   */
  public void exitFrame();
  
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
   * Gets the number of frames registered on the stack
   * @return the number of frames registered on the stack
   */
  public long getTotalFrames();
}
