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
    
    //We reserve the first 8 byte cell to act
    //as the "null" cell.
    
    currentIndex = Long.BYTES;
  }
  
  private void setArray(byte [] target, byte [] toCopy, int startIndex, int endIndex) {
    int i = 0;
    System.out.println(" in "+startIndex+" <-> "+endIndex);
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
        
    final long address = currentIndex;
    
    /*
     * structure layout for non-strings
     * 
     * GC = 0
     * SIZE
     * Element 1
     * ...
     * Element n
     */
    
    System.out.println("*** ALLOCATING STRUCT: "+address);
    
    //place GC data on
    setArray(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(0).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    //place SIZE
    setArray(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong((memberTypeCodes.length << 1) + 1).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    System.out.println("---still allocating ("+(address >>> 1)+")\n"+getHeapRepresentation());
    
    /*
     * top most value is bottom most member 
     */   
    long [] dataArgs = new long[memberTypeCodes.length];
    for (int i = dataArgs.length - 1; i >= 0; i--) {
      dataArgs[i] = stack.popOperand();
      System.out.println("cons arg: "+i+" : "+(dataArgs[i] >>> 1));
    }
    
    for (long l : dataArgs) {
      System.out.println("_____VERIFY: "+(l >>> 1));
    }
    
    for (int i = 0; i < dataArgs.length; i++) {
      byte [] dat = ByteBuffer.allocate(Long.BYTES).putLong(dataArgs[i]).array();
      System.out.print(" putting "+(dataArgs[i] >>> 1));
      setArray(heap, dat, currentIndex, currentIndex + Long.BYTES - 1);
      currentIndex += Long.BYTES;
    }
    
    
    
    usedSpace += totalSizeNeeded;
    
    System.out.println("--ended allocation: "+currentIndex);
    System.out.println("** ALLOCATE STRUCT RESULT: \n"+getHeapRepresentation());
    
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
      System.out.println(getHeapRepresentation());
      throw new OutOfMemoryError("Need "+(currentIndex + totalSizeNeeded)+" bytes!, USED: "+usedSpace+" bytes, MAX: "+heap.length);
    }
    
    long address = currentIndex;
    System.out.println("ADDING AT: "+address);
    //System.out.println(getHeapRepresentation());
    
    //place GC data on
    setArray(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(STRING_GC_MASK).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    //place SIZE
    setArray(heap, 
        ByteBuffer.allocate(Long.BYTES).putLong(( (encoded.length - paddingNeeded) << 1) + 1).array(), 
        currentIndex, 
        currentIndex + Long.BYTES - 1);
    currentIndex += Long.BYTES;
    
    setArray(heap, encoded, currentIndex, currentIndex + encoded.length - 1);
    currentIndex += encoded.length;
    
    System.out.println(" STRING NEW HEAP INDEX: "+currentIndex);
    
    //System.out.println("----PRINTING NEW HEAP");
    //System.out.println(getHeapRepresentation());
    
    return (address << 1);
  }

  @Override
  public String getString(long address) {
    if ( (address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    
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
    if ( (address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    int trueIndex = (int) ((address >>> 1) + (offset * Long.BYTES));
    System.out.println(" ---HEAP GETTING AT IREAL INDEX: "+trueIndex);
    
    byte [] rawValue = Arrays.copyOfRange(heap, trueIndex, trueIndex + Long.BYTES);
    long decoded = ByteBuffer.wrap(rawValue).getLong();
    
    return decoded;
  }

  @Override
  public long mutate(long address, long offset, long newValue) {
    if ( (address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    
    //System.out.println(" ---MUTATING AT REAL INEX");
    int trueIndex = (int) ((address >>> 1) + (offset * Long.BYTES));
    //System.out.println(" ---HEAP GETTING AT IREAL INDEX: "+trueIndex);
    
    byte [] newValueDecoded = ByteBuffer.allocate(Long.BYTES).putLong(newValue).array();
    
    setArray(heap, newValueDecoded, trueIndex, trueIndex + Long.BYTES - 1);
    return address;
  }

  @Override
  public long getSize(long address) {
    if ( (address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    
    /*
     * Assume struct sizes are encoded
     */
    int trueIndex = ((int) (address >>> 1)) + Long.BYTES;
    
    System.out.println("GETTING SIZE AT: "+trueIndex);
    System.out.println("   heap current size: "+heap.length);
    byte [] raw = Arrays.copyOfRange(heap, trueIndex, trueIndex + Long.BYTES);
    
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
    
    final long SIGN_BIT_MASK = 0x0000000000000001L;
    
    for (int i = 0; i <= currentIndex; i += Long.BYTES) {
      long rawValue = ByteBuffer.wrap(Arrays.copyOfRange(heap, i, i + Long.BYTES)).getLong();
      
      x += i+" | "+((rawValue & SIGN_BIT_MASK) == 0 ? "M " : "  ")+(rawValue >>> 1)+"\n";
    }
    
    x += "=======HEAP=======TOTAL:"+heap.length;
    return x;
  }

}
