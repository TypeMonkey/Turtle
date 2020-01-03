package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import jg.cs.runtime.alloc.OperandStack;
import jg.cs.runtime.alloc.err.StackException;
import jg.cs.runtime.alloc.err.StackException.ExceptionCode;

public class DiskOperandStack implements OperandStack{
  
  private final RandomAccessFile stack;
  
  private long currentFileOffet;
  
  public DiskOperandStack(File location) throws FileNotFoundException {
    stack = new RandomAccessFile(location, "rwd");
    currentFileOffet = 0;
  }

  private void growStack() throws IOException {
    stack.setLength(stack.length() + 10);
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
        stack.seek(currentFileOffet - Long.BYTES);

        long val = stack.readLong();
        currentFileOffet -= Long.BYTES;
        return val;
      }
    } catch (IOException e) {
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

}
