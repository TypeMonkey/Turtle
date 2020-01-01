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
 * before any arguments or local variables are stored
 * 
 * When exiting a frame/returing to a caller,
 * it's essential that exitFrame() is called 
 * @author Jose
 *
 */
interface FunctionStack {

  /**
   * Registers a new frame on to the function stack
   */
  void registerFrame();
  
  /**
   * Exits the current frame
   */
  void exitFrame();
  
  /**
   * Retrieves a value from the current frame at a given offset
   * @param offset - the offset, with respect to the current frame
   * @return the value
   * 
   * @throws IllegalArgumentException - if the offset is invalid
   */
  long retrieveAtOffset(long offset) throws IllegalArgumentException;
  
  /**
   * Saves a value to the given offset on the current frame
   * @param offset - the offset, with respect to the current frame
   * @throws IllegalArgumentException - if the offset is invalid
   */
  void saveAtOffset(long offset) throws IllegalArgumentException;
  
  /**
   * Gets the total values stored onto the stack
   * @return the total values stored onto the stack
   */
  long getTotalElements();
  
  /**
   * Gets the number of frames registered on the stack
   * @return the number of frames registered on the stack
   */
  long getTotalFrames();
}
