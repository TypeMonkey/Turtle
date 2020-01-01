package jg.cs.runtime.alloc;

import jg.cs.common.types.Type;

/**
 * Handles the allocation of heap-structures
 * @author Jose
 *
 */
public interface HeapAllocator {
  
  /**
   * Allocates a struct on to the stack
   * @param stack - the FunctionStack to retreieve values from
   * @param memberTypeCodes - the type codes of struct members
   * @param typeCodes - the type codes of all struct definitions
   * @return the starting address of the structure
   */
  long allocate(FunctionStack stack, int [] memberTypeCodes, Type [] typeCodes);
  
  /**
   * Retrieves the value of struct's member
   * @param address - the starting address of the struct
   * @param offset - the offset of the member
   * @return the value of the member
   */
  long get(long address, long offset);
  
  /**
   * Gets the total used size - in bytes - of the heap
   * @return the total used size - in bytes - of the heap
   */
  long getAllocatedSpace();
  
  /**
   * Gets the max size - in bytes - of the heap
   * @return the max size - in bytes - of the heap
   */
  long getMaxSpace();
  
  /**
   * Gets the current address at which the next allocation will be located at
   * @return the current address at which the next allocation will be located at
   */
  long getAllocationIndex();
  
  /**
   * 
   * @return
   */
  String getHeapRepresentation();
  
}
