package jg.cs.runtime.alloc;

import jg.cs.common.types.Type;

/**
 * Handles the allocation of heap-structures
 * @author Jose
 *
 */
public interface HeapAllocator {
  
  /**
   * Allocates a struct on to the heap
   * @param stack - the FunctionStack to retreieve values from
   * @param memberTypeCodes - the type codes of struct members
   * @return the starting address of the structure
   */
  public long allocate(FunctionStack stack, int [] memberTypeCodes) throws OutOfMemoryError;
  
  /**
   * Allocates a string on to the heap
   * 
   * Strings are encoded as ASCII bytes and maybe padded to 
   * fit within a 8-byte space
   * @param string - String to store in the heap
   */
  public long allocate(String string) throws OutOfMemoryError;
  
  /**
   * Retrieves the value of struct's member
   * @param address - the starting address of the struct
   * @param offset - the offset of the member
   * @return the value of the member
   */
  public long get(long address, long offset);
  
  /**
   * Mutates the value of struct's member
   * @param address - the starting address of the struct
   * @param offset - the offset of the member
   * @param newValue - the new value to set this member to
   * @return the address of the struct
   */
  public long mutate(long address, long offset, long newValue);
  
  /**
   * Retrieves the amount of members an element has
   * 
   * Strings are a special case as it is counted byte-by-byte instead 
   * of 8 bytes at a time.
   * 
   * @param address - the address of the struct instance
   * @return the amount of members the struct has (special case for String)
   */
  public long getSize(long address);
  
  /**
   * Gets the total used size - in bytes - of the heap
   * @return the total used size - in bytes - of the heap
   */
  public long getAllocatedSpace();
  
  /**
   * Gets the max size - in bytes - of the heap
   * @return the max size - in bytes - of the heap
   */
  public long getMaxSpace();
  
  /**
   * Gets the current address at which the next allocation will be located at
   * @return the current address at which the next allocation will be located at
   */
  public long getAllocationIndex();
  
  /**
   * Returns a text-friendly representation of the heap
   * @return a text-friendly representation of the heap
   */
  public String getHeapRepresentation();
  
}
