package jg.cs.runtime.alloc.mem;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;

public class MemHeapAllocator implements HeapAllocator{
  
  private static final long STRING_GC_MASK = 0x00000001;

  private final long [] heap;
  
  private int currentIndex;
  private int usedSpace;
  
  public MemHeapAllocator(int maxSize) {
    heap = new long[Math.round(maxSize / 8)];
    currentIndex = 0;
  }
  
  @Override
  public long allocate(FunctionStack stack, int[] memberTypeCodes) {
    // TODO Auto-generated method stub
    int totalSizeNeeded = currentIndex + 2 + memberTypeCodes.length;
    if (totalSizeNeeded > heap.length - 1) {
      throw new OutOfMemoryError("Need "+totalSizeNeeded+" bytes!, USED: "+usedSpace+" bytes");
    }
    
    long address = currentIndex;
    
    /*
     * structure layout for non-strings
     * 
     * GC = 0
     * SIZE
     * Element 1
     * ...
     * Element n
     */
    heap[currentIndex] = 0;
    heap[++currentIndex] = (memberTypeCodes.length << 1) + 1; //encode member size
    
    /*
     * top most value is bottom most member 
     */   
    int offset = 0;
    for (int i = currentIndex + memberTypeCodes.length; i > 0; i--) {
      heap[currentIndex] = stack.retrieveAtOffset(offset);
      currentIndex++;
      offset++;
    }
    
    usedSpace += totalSizeNeeded;
    
    return address;
  }

  @Override
  public long allocate(String string) {
    /*
     * GC Word for Strings are different. 
     * 
     * Layout
     * GC = 1 <--- right most bit of GC for all heap structs signify whether it's a string or not
     *        1 <-- string
     *        0 <-- non-string
     * 
     * SIZE = charcater amount in ASCII
     * bytes....
     */
    byte [] encoded = string.getBytes(StandardCharsets.US_ASCII);
    
    if (currentIndex + Math.floor(encoded.length / 8) > heap.length - 1) {
      throw new OutOfMemoryError("Need "+(currentIndex + Math.floor(encoded.length / 8))+" bytes!, USED: "+usedSpace+" bytes");
    }
    
    long startAddress = currentIndex;
    
    heap[currentIndex] = STRING_GC_MASK;
    heap[++currentIndex] = (encoded.length << 1) + 1;
    
    encoded = Arrays.copyOf(encoded, encoded.length + (8 % encoded.length));
    
    for (int i = 0; i < encoded.length; i += 8) {
      heap[currentIndex] = ByteBuffer.wrap(Arrays.copyOfRange(encoded, i, i + 8)).getLong();         
      currentIndex++;
    }
    
    return startAddress;
  }

  @Override
  public long get(long address, long offset) {
    return heap[(int) (address + offset + 1)];
  }

  @Override
  public long mutate(long address, long offset, long newValue) {
    heap[(int) (address + offset + 1)] = newValue;
    return address;
  }

  @Override
  public long getSize(long address) {
    /*
     * Assume struct sizes are encoded
     */
    return heap[(int) (address+1)];
  }

  @Override
  public long getAllocatedSpace() {
    return usedSpace;
  }

  @Override
  public long getMaxSpace() {
    // TODO Auto-generated method stub
    return heap.length;
  }

  @Override
  public long getAllocationIndex() {
    return currentIndex;
  }

  @Override
  public String getHeapRepresentation() {
    // TODO Auto-generated method stub
    return null;
  }

}
