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

  public DiskFunctionStack(File file) throws IOException {
    stack = new RandomAccessFile(file, "rwd");
    stack.setLength(80);
    stack.seek(0);
    index = 0;
  }
  
  @Override
  public void changeFPBy(long insIndex) {
    System.out.println("  ---> FP THEN: "+index);
    index += (insIndex * Long.BYTES);
    System.out.println("  ---> FP NOW: "+index);
  }
  
  @Override
  public void setFP(long fp) {
    System.out.println("  ---> FP SET THEN: "+index);
    index = (fp * Long.BYTES);
    System.out.println("  ---> FP SET NOW: "+index);
  }
  
  private void growStack() {
    try {
      stack.setLength(stack.length() + 80);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public long retrieveAtOffset(long offset) throws IllegalArgumentException, StackException {
    try {
      final long actualOffset = offset * Long.BYTES;
      System.out.println("----RETREIVING FROM RI: "+(index + offset));
      stack.seek(index + actualOffset);
      long val =  stack.readLong();
      System.out.println("     ---val: "+val+" | dec? "+(val >>> 1));
      System.out.println(toString(offset));
      return val;
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public void saveAtOffset(long offset, long value) throws IllegalArgumentException, StackException {
    try {
      System.out.println("^^^^^^^^^^^^^^WRITING TO RI: "+getRealAddress(offset)+" is val "+(value >>> 1));
      final long actualOffset = offset * Long.BYTES;
      if (index + actualOffset > stack.length() - 1) {
        growStack();
        saveAtOffset(offset, value);
      }
      else {
        stack.seek(index + actualOffset);
        stack.writeLong(value);
        
        System.out.println(toString(offset));     
      }      
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public long getRealAddress(long offset) {
    return index + (offset * Long.BYTES);
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
    return index;
  }
  
  public String toString(long offset) {
    try {
      System.out.println("----SIZE: "+stack.length());
      String x = "==================DFSTACK==================TOTAL: "+stack.length()+"\n";
      x += "  index = "+index+" | offset = "+offset+"\n";
      for (int i = (int) index; i <= index + (offset * Long.BYTES); i += 8) {
        stack.seek(i);
        x += i+" : "+(stack.readLong() >>> 1)+" \n";
      }
      x += "==================DFSTACK END==================\n";
      return x;
    } catch (IOException e) {
      e.printStackTrace();
      return "IO ERROR COULD NOT READ STACK "+e.getMessage();
    }
  }
}
