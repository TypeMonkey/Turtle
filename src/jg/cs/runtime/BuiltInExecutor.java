package jg.cs.runtime;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import jg.cs.common.BuiltInFunctions;
import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;
import jg.cs.runtime.alloc.OperandStack;

public class BuiltInExecutor {

  public static void executeBuiltIn(int riCode, FunctionStack stack, OperandStack opStack, HeapAllocator heap) {
    BuiltInFunctions function = BuiltInFunctions.values()[riCode];

    long [] values = gatherArguments(opStack, function.getIdentity().getSignature().getParameterTypes().length);
    System.out.print("ARGS: [");
    for (long l : values) {
      System.out.print((l >>> 1)+", ");
    }
    System.out.print("]\n");
    
    System.out.println(stack);
    
    if (function == BuiltInFunctions.INPUT) {
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      opStack.pushOperand(heap.allocate(input));
    }
    else if (function == BuiltInFunctions.PRINT) {
      System.out.print("<<<<<<<<<<"+heap.getString(values[0])+">>>>>>>>>>>>>");
      opStack.pushOperand(values[0]);
    }
    else if (function == BuiltInFunctions.PRINTLN) {
      System.out.println("<<<<<<<<<<"+heap.getString(values[0])+">>>>>>>>>>");
      opStack.pushOperand(values[0]);
    }
    else if (function == BuiltInFunctions.INC) {            
      opStack.pushOperand(values[0] + 2);
    }
    else if (function == BuiltInFunctions.DEC) {
      opStack.pushOperand(values[0] - 2);
    }
    else if (function == BuiltInFunctions.LEN) {
      opStack.pushOperand(heap.get(values[0], 1));
    }
    else if (function == BuiltInFunctions.TO_STR_I) {
      opStack.pushOperand((Long.parseLong(heap.getString(values[0])) << 1) + 1);
    }
    else if (function == BuiltInFunctions.TO_STR_B) {
      opStack.pushOperand(values[0] == Executor.TRUE ? heap.allocate("true") : heap.allocate("false"));
    }
  }
  
  private static long[] gatherArguments(OperandStack stack, int size) {
    long [] args = new long[size];
    
    System.out.println("---ON STADCK BUILT IN: "+stack);
    for (int i = args.length - 1; i >= 0; i--) {
      args[i] = stack.popOperand();
    }
    
    return args;
  }
}
