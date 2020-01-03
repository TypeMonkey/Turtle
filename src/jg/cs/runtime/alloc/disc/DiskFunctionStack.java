package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.err.StackException;
import jg.cs.runtime.alloc.err.StackException.ExceptionCode;

public class DiskFunctionStack implements FunctionStack{
  
  private final RandomAccessFile stack;
  
  private long index;

  public DiskFunctionStack(File file) throws FileNotFoundException {
    stack = new RandomAccessFile(file, "rwd");
    index = 0;
  }
  
  @Override
  public void changeFPBy(long insIndex) {
    //System.out.println("  ---> FP THEN: "+index);
    index += insIndex;
    //System.out.println("  ---> FP NOW: "+index);
  }
  
  @Override
  public void setFP(long fp) {
    //System.out.println("  ---> FP SET THEN: "+index);
    index = (int) fp;
    //System.out.println("  ---> FP SET NOW: "+index);
  }

  @Override
  public long retrieveAtOffset(long offset) throws IllegalArgumentException, StackException {
    try {
      stack.seek(index + offset);
      return stack.readLong();
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public void saveAtOffset(long offset, long value) throws IllegalArgumentException, StackException {
    try {
      stack.seek(index + offset);
      stack.writeLong(value);
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public long getRealAddress(long offset) {
    return index + offset;
  }

  @Override
  public long getTotalElements() {
    try {
      return stack.length() / Long.BYTES;
    } catch (IOException e) {
      throw new Error("IO ERROR when getting file size");
    }
  }

  @Override
  public long getCurrentFP() {
    // TODO Auto-generated method stub
    return index;
  }
}
