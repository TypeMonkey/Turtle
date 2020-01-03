package jg.cs.runtime.alloc.mem;

import java.util.Arrays;
import java.util.Stack;

import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.FunctionStack;

public class MemFunctionStack implements FunctionStack{
  
  private long [] fstack;
  
  private int index;
  
  public MemFunctionStack() {
    fstack = new long[10];
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
  
  private void growStack() {
    fstack = Arrays.copyOf(fstack, fstack.length + 10);
  }

  @Override
  public long retrieveAtOffset(long offset) throws IllegalArgumentException {
    //System.out.println(" --< FSTACK: RETREIVING AT: "+(index + offset));
    return fstack[(int) (index + offset)];
  }

  @Override
  public void saveAtOffset(long offset, long value) throws IllegalArgumentException {
    if (index + offset > fstack.length - 1) {
      growStack();
      saveAtOffset(offset, value);
    }
    else {
      //System.out.println(" --< FSTACK: SAVING AT AT: "+(index + offset)+" | "+value);
      fstack[(int) (index + offset)] = value;
    }
  }
  
  @Override
  public long getCurrentFP() {
    return index;
  }

  @Override
  public long getTotalElements() {
    // TODO Auto-generated method stub
    return index;
  }
  
  @Override
  public long getRealAddress(long offset) {
    return offset + index;
  }

  @Override
  public String toString() {
    String x = "=======STACK=======\n";
    
    final long SIGN_BIT_MASK = 0x8000000000000000L;
    
    for (int i = fstack.length - 1; i >= 0; i--) {
      long val = fstack[i] & Executor.TAG_MASK; 
      long signBit = fstack[i] & SIGN_BIT_MASK;
      x += i+" | "+(val == 1 ? ((fstack[i] >>> 1) | signBit) : Long.toHexString(fstack[i]))+"\n";
    }
    
    x += "=======STACK=======";
    return x;
  }
}
