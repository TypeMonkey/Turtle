package jg.cs.runtime.alloc.disc;

import java.io.File;

public class Main {
  
  public static void main(String[] args) throws Exception {
    File file = new File("OP.ST");
    file.createNewFile();
    
    DiskOperandStack operandStack = new DiskOperandStack(file);
    
    operandStack.pushOperand(10);
    operandStack.pushOperand(1);
    
    System.out.println(operandStack.popOperand());
    System.out.println(operandStack.popOperand());
    
    operandStack.pushOperand(1);
    System.out.println(operandStack.popOperand());
    
    operandStack.pushOperand(6);
    System.out.println(operandStack.popOperand());
    
    operandStack.pushOperand(7);
    System.out.println(operandStack.popOperand());
  }
  
}
