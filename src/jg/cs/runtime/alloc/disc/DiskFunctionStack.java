package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.err.StackException;
import jg.cs.runtime.alloc.err.StackException.ExceptionCode;
import jg.cs.runtime.alloc.mem.MemFunctionStack;

public class DiskFunctionStack extends MemFunctionStack{
  
  private long index;
  
  public DiskFunctionStack() {
    super();
    index = 0;
  }
  
  @Override
  public void setFP(final long fp) {
    super.setFP(fp);
    
    System.err.println("--doffset THEN: "+index);
    index += (fp * Long.BYTES);
    System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public void changeFPBy(final long insIndex) {
    super.changeFPBy(insIndex);
    
    System.err.println("--doffset THEN: "+index);
    index += (insIndex * Long.BYTES);
    System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public long retrieveAtOffset(final long offset) throws IllegalArgumentException {
    return super.retrieveAtOffset(offset);
    
    System.err.println("--doffset THEN: "+index);
    index += (fp * Long.BYTES);
    System.err.println("--doffset NOW: "+index);
  }
  
  @Override
  public void saveAtOffset(final long offset, final long value) throws IllegalArgumentException {
    super.saveAtOffset(offset, value);
  }
  
  @Override
  public long getRealAddress(final long offset) {
    return super.getRealAddress(offset);
  }
  
  @Override
  public long getTotalElements() {
    return super.getTotalElements();
  }
  
  @Override
  public long getCurrentFP() {
    return super.getCurrentFP();
  }
}
