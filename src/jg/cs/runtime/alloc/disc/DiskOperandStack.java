package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.stream.Collectors;

import jg.cs.runtime.alloc.OperandStack;
import jg.cs.runtime.alloc.err.StackException;
import jg.cs.runtime.alloc.err.StackException.ExceptionCode;

public class DiskOperandStack implements OperandStack{
  
  private final RandomAccessFile stack;
  
  private long currentFileOffet;
  
  public DiskOperandStack(File location) throws IOException {
    stack = new RandomAccessFile(location, "rwd");
    stack.setLength(0);
    currentFileOffet = 0;
  }

  private void growStack() throws IOException {
    stack.setLength(stack.length() + (Long.BYTES * 10));
  }
  
  @Override
  public void pushOperand(long value) throws StackException{
    try {
      if (currentFileOffet + Long.BYTES > stack.length() - 1) {
        growStack();
        pushOperand(value);
      }
      else {
        stack.seek(currentFileOffet);
        stack.writeLong(value);
        currentFileOffet += Long.BYTES;
      }
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public long popOperand() throws StackException {
    try {
      if (stack.length() == 0) {
        throw new StackException(ExceptionCode.EMPTY_STACK);
      }
      else {
        
        //System.out.println(" ---CURRENT FO: "+currentFileOffet);
        stack.seek(currentFileOffet - Long.BYTES);

        long val = stack.readLong();
        currentFileOffet -= Long.BYTES;
        return val;
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public int stackSize() {
    // TODO Auto-generated method stub
    return (int) currentFileOffet;
  }

  @Override
  public void clear() {
    try {
      stack.setLength(0);
    } catch (IOException e) {
      throw new StackException(ExceptionCode.IO_ERROR);
    }
  }

  @Override
  public String toString() {
    String s = "=====OPERAND STACK=====\n";
    s += "BOTTOM\n";
  
    
    try {
      stack.seek(0);
      
      int i = 0;
      while (i < currentFileOffet) {
        s += " ["+(stack.readLong() >>> 1)+"]\n";
        i += Long.BYTES;
      }
      
      s += "TOP\n";

      stack.seek(currentFileOffet);
    } catch (IOException e) {
      e.printStackTrace();
    }
        
    return s;
  }
}
