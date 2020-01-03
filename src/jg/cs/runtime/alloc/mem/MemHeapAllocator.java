package jg.cs.runtime.alloc.mem;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;
import jg.cs.runtime.alloc.OperandStack;

public class MemHeapAllocator implements HeapAllocator{

  private final byte [] heap;
  
  private int currentIndex;
  private int usedSpace;
  
  public MemHeapAllocator(int maxSize) {
    heap = new byte[maxSize];
    currentIndex = 0;
  }
  
  private void setArraySet(byte [] target, byte [] toCopy, int startIndex, int endIndex) {
    int i = 0;
    for ( ; startIndex <= endIndex; startIndex++) {
      target[startIndex] = toCopy[i];
      i++;
    }
  }
  
  @Override
  public long allocate(OperandStack stack, int[] memberTypeCodes) {
    // TODO Auto-generated method stub
    int totalSizeNeeded = META_DATA_SIZE + (memberTypeCodes.length * Long.BYTES);
    if (currentIndex + totalSizeNeeded > heap.length - 1) {
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
    
    //place GC data on
    setArraySet(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(0).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    //place SIZE
    setArraySet(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong((memberTypeCodes.length << 1) + 1).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    /*
     * top most value is bottom most member 
     */   
    for (int i = currentIndex + memberTypeCodes.length; i > 0; i--) {
      long operand = stack.popOperand();
      setArraySet(heap, 
          ByteBuffer.allocate(Long.BYTES).putLong(operand).array(), 
          currentIndex, 
          currentIndex + Long.BYTES - 1);
      currentIndex += Long.BYTES;
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
    System.out.println("ALLOCATING STTRING |"+string+"|");

    byte [] encoded = string.getBytes();
    
    int paddingNeeded = Long.BYTES % encoded.length;
    paddingNeeded = Long.BYTES - (encoded.length % Long.BYTES );
    
    //System.out.println("--- RAW: "+encoded.length+" | PADDING: "+paddingNeeded);
    
    //pad byte encoding
    encoded = Arrays.copyOfRange(encoded, 0, encoded.length + paddingNeeded);
    
    //System.out.println("---- NEW: "+Arrays.toString(encoded));
    
    assert (encoded.length % Long.BYTES) == 0;
    
    int totalSizeNeeded = encoded.length + paddingNeeded + META_DATA_SIZE;
    if (currentIndex + totalSizeNeeded > heap.length - 1) {
      throw new OutOfMemoryError("Need "+totalSizeNeeded+" bytes!, USED: "+usedSpace+" bytes");
    }
    
    long address = currentIndex;
    //System.out.println("ADDING AT: "+address);
    //System.out.println(getHeapRepresentation());
    
    //place GC data on
    setArraySet(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(STRING_GC_MASK).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    //place SIZE
    setArraySet(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(( (encoded.length - paddingNeeded) << 1) + 1).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    setArraySet(heap, encoded, currentIndex, currentIndex + encoded.length - 1);
    currentIndex += encoded.length;
    
    //System.out.println("----PRINTING NEW HEAP");
    //System.out.println(getHeapRepresentation());
    
    return (address << 1);
  }

  @Override
  public String getString(long address) {
    int trueIndex = (int) (address >>> 1);
    //System.out.println("RETREIVING STRING------- "+trueIndex);
    //System.out.println(getHeapRepresentation());
    
    long size = getSize(address) >>> 1;  //size is encoded, must be decoded

    //System.out.println("  ---STRING ADDR: "+trueIndex+" | SIZE: "+size);
    
    int stringStart = trueIndex + (2 * Long.BYTES);
    
    
    //System.out.println(" s: "+stringStart+" , e:"+(stringStart + size));
    byte [] stringBytes = Arrays.copyOfRange(heap, stringStart, (int) (stringStart + size));
    //System.out.println("   --- GOT "+stringBytes.length+" bytes");
      
    return new String(stringBytes);
  }
  
  @Override
  public long get(long address, long offset) {
    int trueIndex = (int) ((address >>> 1) + (offset * offset));
    //System.out.println(" ---HEAP GETTING AT IREAL INDEX: "+trueIndex);
    
    byte [] rawValue = Arrays.copyOfRange(heap, trueIndex, trueIndex + Long.BYTES);
    long decoded = ByteBuffer.wrap(rawValue).getLong();
    
    return decoded;
  }

  @Override
  public long mutate(long address, long offset, long newValue) {
    //System.out.println(" ---MUTATING AT REAL INEX");
    int trueIndex = (int) ((address >>> 1) + offset);
    //System.out.println(" ---HEAP GETTING AT IREAL INDEX: "+trueIndex);
    
    byte [] newValueDecoded = ByteBuffer.allocate(Long.BYTES).putLong(newValue).array();
    
    setArraySet(heap, newValueDecoded, trueIndex, trueIndex + Long.BYTES - 1);
    return address;
  }

  @Override
  public long getSize(long address) {
    /*
     * Assume struct sizes are encoded
     */
    int trueIndex = ((int) (address >>> 1)) + Long.BYTES;
    
    //System.out.println("GETTING SIZE AT: "+trueIndex);
    
    byte [] raw = Arrays.copyOfRange(heap, trueIndex, trueIndex + Long.BYTES );
    
    return ByteBuffer.wrap(raw).getLong();
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
    
    for (int i = 0; i <= currentIndex; i += Long.BYTES) {
      long rawValue = ByteBuffer.wrap(Arrays.copyOfRange(heap, i, i + Long.BYTES)).getLong();
      
      long val = rawValue & Executor.TAG_MASK; 
      long signBit = rawValue & SIGN_BIT_MASK;
      x += i+" | "+(val == 1 ? ((rawValue >>> 1) | signBit) : Long.toHexString(rawValue))+"\n";
    }
    
    x += "=======HEAP=======TOTAL:"+heap.length;
    return x;
  }

}
