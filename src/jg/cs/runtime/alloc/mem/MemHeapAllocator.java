package jg.cs.runtime.alloc.mem;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;

public class MemHeapAllocator implements HeapAllocator{
  
  private static final long STRING_GC_MASK = 0x0000000000000001L;

  private final long [] heap;
  
  private int currentIndex;
  private int usedSpace;
  
  public MemHeapAllocator(int maxSize) {
    heap = new long[Math.round(maxSize / 8)];
    currentIndex = 1;
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
    
    return (address << 1);
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
    
    if (currentIndex + Math.floor(encoded.length / 8) + 2  > heap.length - 1) {
      throw new OutOfMemoryError("Need "+(currentIndex + Math.floor(encoded.length / 8))+" bytes!, USED: "+usedSpace+" bytes");
    }
    
    System.out.println(" >>>>>>>ALLOCATING: "+string+" | "+currentIndex);
    
    long startAddress = currentIndex;
    
    heap[currentIndex] = STRING_GC_MASK;
    heap[currentIndex + 1] = (encoded.length << 1) + 1;
    
    System.out.println("---encoded size: "+heap[currentIndex + 1]);
    
    currentIndex += 2;
    
    System.out.println("  length index: "+currentIndex);
    
    int padding = (8 % encoded.length) != 0 ? (8 - encoded.length) : 0;    
    encoded = Arrays.copyOf(encoded, encoded.length + padding);
    System.out.println("- size: "+encoded.length);
    
    
    for (int i = 0; i < encoded.length; i += 8) {
      heap[currentIndex] = ByteBuffer.wrap(Arrays.copyOfRange(encoded, i, i + 8)).getLong();  
      System.out.println("---BYTES: "+ByteBuffer.wrap(Arrays.copyOfRange(encoded, i, i + 8)).getLong());
      System.out.println("   -> RED: "+heap[currentIndex]+" | "+currentIndex+" | "+i);
      currentIndex++;
    }
    
    usedSpace += 2 + (encoded.length / 8);
    
    System.out.println("-- consts: "+heap[0]+" | "+heap[1]+" | "+heap[2]);
    
    System.out.println("---ALLOCATING STRING: "+string+" | GIVING ADDR: "+startAddress);
    
    System.out.println(getHeapRepresentation());
    
    return (startAddress << 1);
  }

  @Override
  public long get(long address, long offset) {
    System.out.println(" ---HEAP GETTING AT IREAL INDEX: "+((int) ( (address>>>1) + offset)));
    return heap[(int) ( (address>>>1) + offset)];
  }

  @Override
  public long mutate(long address, long offset, long newValue) {
    heap[(int) ((address>>>1) + offset + 1)] = newValue;
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
    String x = "=======HEAP=======TAKEN:"+usedSpace+"\n";
    
    final long SIGN_BIT_MASK = 0x8000000000000000L;
    
    for (int i = 1; i <= currentIndex; i++) {
      long val = heap[i] & Executor.TAG_MASK; 
      long signBit = heap[i] & SIGN_BIT_MASK;
      x += i+" | "+(val == 1 ? ((heap[i] >>> 1) | signBit) : Long.toHexString(heap[i]))+"\n";
    }
    
    x += "=======HEAP=======TOTAL:"+heap.length;
    return x;
  }

}
