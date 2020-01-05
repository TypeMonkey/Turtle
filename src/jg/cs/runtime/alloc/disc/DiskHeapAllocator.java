package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.HeapAllocator;
import jg.cs.runtime.alloc.OperandStack;

public class DiskHeapAllocator implements HeapAllocator{
  
  private final RandomAccessFile heap;
  private final long maxSize;
  
  private long currentIndex;
  private long usedSize;
  
  public DiskHeapAllocator(File targetFile, long maxSize) throws IOException {
    this.maxSize = maxSize;
    heap = new RandomAccessFile(targetFile, "rwd");
    heap.setLength(maxSize);
    
    currentIndex = Long.BYTES;
    heap.seek(currentIndex);
  }

  @Override
  public long allocate(OperandStack stack, int[] memberTypeCodes) throws OutOfMemoryError {
    // TODO Auto-generated method stub
    int totalSizeNeeded = META_DATA_SIZE + (memberTypeCodes.length * Long.BYTES);
    if (currentIndex + totalSizeNeeded > maxSize - 1) {
      throw new OutOfMemoryError("Need "+totalSizeNeeded+" bytes!, USED: "+usedSize+" bytes");
    }
        
    final long address = currentIndex;
    
    //System.out.println("~~~~~~~ALLOCATING AT :"+address);
    
    /*
     * structure layout for non-strings
     * 
     * GC = 0
     * SIZE
     * Element 1
     * ...
     * Element n
     */
    
    try {
      //place GC data on
      heap.seek(currentIndex);
      heap.writeLong(0);
      currentIndex += Long.BYTES;
      
      //place SIZE
      heap.seek(currentIndex);
      heap.writeLong((memberTypeCodes.length << 1) + 1);
      currentIndex += Long.BYTES;
      
      
      /*
       * top most value is bottom most member 
       */   
      long [] dataArgs = new long[memberTypeCodes.length];
      for (int i = dataArgs.length - 1; i >= 0; i--) {
        dataArgs[i] = stack.popOperand();
        //System.out.println("cons arg: "+i+" : "+(dataArgs[i] >>> 1));
      }
      
      
      for (long l : dataArgs) {
        //System.out.println("_____VERIFY: "+(l >>> 1));
      }
      
      
      for (int i = 0; i < dataArgs.length; i++) {
        heap.seek(currentIndex);
        heap.writeLong(dataArgs[i]);
        //System.out.print(" putting "+(dataArgs[i] >>> 1));
        currentIndex += Long.BYTES;
      }
      
      usedSize += totalSizeNeeded;      
      return (address << 1);
    } catch (IOException e) {
      throw new Error("IO Error reading from disk heap. Message: \n"+e.getMessage());
    }
  }

  @Override
  public long allocate(String string) throws OutOfMemoryError {
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
    //System.out.println("ALLOCATING ON DISK STTRING |"+string+"|");

    byte [] encoded = string.getBytes();
    
    //System.out.println("---- DISK: "+encoded.length);
    
    int paddingNeeded = Long.BYTES % encoded.length;
    paddingNeeded = Long.BYTES - (encoded.length % Long.BYTES );
    
    //System.out.println("--- RAW: "+encoded.length+" | PADDING: "+paddingNeeded);
    
    //pad byte encoding
    encoded = Arrays.copyOfRange(encoded, 0, encoded.length + paddingNeeded);
    
    //System.out.println("---- NEW: "+Arrays.toString(encoded));
    
    assert (encoded.length % Long.BYTES) == 0;
    
    int totalSizeNeeded = encoded.length + paddingNeeded + META_DATA_SIZE;
    if (currentIndex + totalSizeNeeded > maxSize - 1) {
      throw new OutOfMemoryError("Need "+totalSizeNeeded+" bytes!, USED: "+usedSize+" bytes");
    }
    
    try {
      long address = currentIndex;
      //System.out.println("ADDING AT: "+address);
      //System.out.println(getHeapRepresentation());
      
      //place GC data on
      heap.seek(currentIndex);
      heap.writeLong(STRING_GC_MASK);
      currentIndex += Long.BYTES;
      
      //place SIZE
      heap.seek(currentIndex);
      heap.writeLong(((encoded.length - paddingNeeded) << 1) + 1);
      //System.out.println("-- WROTE SIZE: of "+(((encoded.length - paddingNeeded) << 1) + 1)+" AT index: "+currentIndex);
      //System.out.println("           whole size: "+encoded.length+" , padding: "+paddingNeeded);
      currentIndex += Long.BYTES;
      
      heap.seek(currentIndex);
      heap.write(encoded);
      currentIndex += encoded.length;
      
      //System.out.println("---FP NOW AT: "+heap.getFilePointer());
      
      //System.out.println("----PRINTING NEW HEAP");
      //System.out.println(getHeapRepresentation());
      
      return (address << 1);
    } catch (IOException e) {
      throw new Error("IO Error reading from disk heap. Message: \n"+e.getMessage());
    }
  }

  @Override
  public String getString(long address) {
    long trueIndex = (address >>> 1);
    if (trueIndex == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    //System.out.println("RETREIVING DISK STRING------- "+trueIndex);
    //System.out.println(getHeapRepresentation());

    long size = getSize(address) >>> 1;  //size is encoded, must be decoded
      
    //this size does not include padding, only character count
    long paddingNeeded = Long.BYTES % size;
    paddingNeeded = Long.BYTES - (size % Long.BYTES );

    try {
      //System.out.println("  ---STRING ADDR: "+trueIndex+" | SIZE: "+size+" | OFFSET: "+heap.getFilePointer());
      //System.out.println("         PADDING: "+paddingNeeded);
      
      //int stringStart = trueIndex + (2 * Long.BYTES);

      //System.out.println(" s: "+stringStart+" , e:"+(stringStart + size));
      byte [] stringBytes = new byte[(int) (size + paddingNeeded)];
      heap.seek(trueIndex + (2 * Long.BYTES));
      heap.read(stringBytes);
      
      heap.seek(currentIndex);
      
      return new String(stringBytes, 0, (int) size);
    } catch (IOException e) {
        // TODO: handle exception
      throw new Error("IO Error reading from disk heap. Message: \n"+e.getMessage());
    }
  }

  @Override
  public long get(long address, long offset) {
    if ((address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    long trueIndex = ((address >>> 1) + (offset * Long.BYTES));
    
    try {
      heap.seek(trueIndex);
      long val = heap.readLong();
      heap.seek(currentIndex);
      return val;
    } catch (IOException e) {
      throw new Error("IO Error reading from disk heap. Index attemp: "+trueIndex);
    }  
  }

  @Override
  public long mutate(long address, long offset, long newValue) {
    if ((address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    
    try {
      long trueIndex = ((address >>> 1) + (offset * Long.BYTES));
      heap.seek(trueIndex);
      
      heap.writeLong(newValue);
      
      heap.seek(currentIndex);
      
      return address;
    } catch (Exception e) {
      throw new Error("IO Error reading from disk heap. Message: \n"+e.getMessage());
    }
  }

  @Override
  public long getSize(long address) {
    if ((address >>> 1) == 0) {
      throw new Error("Error: Attempt to access a null address");
    }
    try {
      long trueIndex = (address >>> 1) + Long.BYTES ;
      //System.out.println("  ***** READING SIZE: "+trueIndex);
      heap.seek(trueIndex);     
      long size = heap.readLong();
      heap.seek(currentIndex);
      return size;
    } catch (Exception e) {
      throw new Error("IO Error reading from disk heap. Message: \n"+e.getMessage());
    }
  }

  @Override
  public long getAllocatedSpace() {
    return usedSize;
  }

  @Override
  public long getMaxSpace() {
    return maxSize;
  }

  @Override
  public long getAllocationIndex() {
    return currentIndex;
  }

  @Override
  public String getHeapRepresentation() {    
    try {
      String x = "=======DHEAP=======TAKEN:"+usedSize+"\n";

      final long SIGN_BIT_MASK = 0x8000000000000000L;

      heap.seek(0);
      
      for (int i = 0; i <= currentIndex; i += Long.BYTES) {
        long rawValue = heap.readLong();

        long val = rawValue & Executor.TAG_MASK; 
        long signBit = rawValue & SIGN_BIT_MASK;
        x += i+" | "+(val == 1 ? ((rawValue >>> 1) | signBit) : Long.toHexString(rawValue))+"\n";
      }

      x += "=======HEAP=======TOTAL:"+maxSize;
      
      heap.seek(currentIndex);
      
      return x;
    } catch (IOException e) {
      throw new Error("ERROR PRINTING DISK HEAP!");
    }
  }

}
