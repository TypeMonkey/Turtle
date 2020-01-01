package jg.cs.runtime.alloc.mem;

import java.util.Stack;

import jg.cs.runtime.alloc.FunctionStack;

public class MemFunctionStack implements FunctionStack{
  
  private final Stack<Long> fStack;
  
  private long frames;
  
  public MemFunctionStack() {
    fStack = new Stack<>();
  }

  @Override
  public void registerFrame() {
    // TODO Auto-generated method stub
    
    frames++;
  }

  @Override
  public void exitFrame() {
    // TODO Auto-generated method stub
    
    frames--;
  }

  @Override
  public long retrieveAtOffset(long offset) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void saveAtOffset(long offset, long value) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public long getTotalElements() {
    // TODO Auto-generated method stub
    return fStack.size();
  }

  @Override
  public long getTotalFrames() {
    return frames;
  }

}
