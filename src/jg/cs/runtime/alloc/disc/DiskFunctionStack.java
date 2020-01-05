package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.err.StackException;
import jg.cs.runtime.alloc.err.StackException.ExceptionCode;
import jg.cs.runtime.alloc.mem.MemFunctionStack;

public class DiskFunctionStack implements FunctionStack{
  
  private final DiskArray diskArray;
  
  private long index;
  
  public DiskFunctionStack(File storage) throws IOException {
    diskArray = new DiskArray(storage, Long.BYTES);
    index = 0;
  }
  
  @Override
  public void setFP(final long fp) {
    //super.setFP(fp);    
    //System.err.println("--doffset THEN: "+index);
    index = fp;
    //System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public void changeFPBy(final long insIndex) {
    //super.changeFPBy(insIndex);
    
    //System.err.println("--doffset THEN: "+index);
    index += insIndex;
    //System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public long retrieveAtOffset(final long offset) throws IllegalArgumentException {
    //return super.retrieveAtOffset(offset);
    
    try {
      return diskArray.get(index + offset);
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
    
    //System.err.println("--doffset THEN: "+index);
    //index += (offset * Long.BYTES);
    //System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public void saveAtOffset(final long offset, final long value) throws IllegalArgumentException {
    try {
      diskArray.set(index + offset, value);
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }
  
  @Override
  public long getRealAddress(final long offset) {
    return offset + index;
  }
  
  @Override
  public long getTotalElements() {
    try {
      return diskArray.maxIndex() + 1;
    } catch (IOException e) {
      e.printStackTrace();
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }
  
  @Override
  public long getCurrentFP() {
    return index;
  }
}
