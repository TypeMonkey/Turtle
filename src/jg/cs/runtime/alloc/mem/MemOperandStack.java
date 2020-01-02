package jg.cs.runtime.alloc.mem;

import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.stream.Collectors;

import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.OperandStack;

public class MemOperandStack implements OperandStack{
  
  private final Stack<Long> stack;
  
  public MemOperandStack() {
    stack = new Stack<>();
  }

  @Override
  public void pushOperand(long value) {
    // TODO Auto-generated method stub
    stack.push(value);
  }

  @Override
  public long popOperand() throws NoSuchElementException {
    return stack.pop();
  }

  @Override
  public int stackSize() {
    return stack.size();
  }

  @Override
  public void clear() {
    stack.clear();
  }
  
  @Override
  public String toString() {
    return "=====OPERAND STACK=====\n["+stack.stream().map(x -> (x >>> 1)+"").collect(Collectors.joining(","))+"]";
  }
}
