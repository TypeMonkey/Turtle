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
    System.out.println("ARGS: "+Arrays.toString(values));
    
    System.out.println(stack);
    
    if (function == BuiltInFunctions.INPUT) {
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      opStack.pushOperand(heap.allocate(input));
    }
    else if (function == BuiltInFunctions.PRINT) {
      System.out.print(decodeString(values[0], heap)+" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
      opStack.pushOperand(values[0]);
    }
    else if (function == BuiltInFunctions.PRINTLN) {
      System.out.println(decodeString(values[0], heap)+" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
      opStack.pushOperand((Long.parseLong(decodeString(values[0], heap)) << 1) + 1);
    }
    else if (function == BuiltInFunctions.TO_STR_B) {
      opStack.pushOperand(values[0] == Executor.TRUE ? heap.allocate("true") : heap.allocate("false"));
    }
  }
  
  private static String decodeString(long address, HeapAllocator heap) {
    System.out.println(heap.getHeapRepresentation());
    
    long size = heap.get(address, 1) >>> 1;  //size is encoded, must be decoded

    System.out.println("  ---STRING ADDR: "+address+" | SIZE: "+size);
      
    String b = "";
    
    int segments = (int) Math.ceil( ((double) size / 8)) ;
    System.out.println("  ---calculated segs: "+segments+" | true value: "+((double) size / 8));
    for(int i = 0; i < segments; i++) {
      long segment = heap.get(address, i + 2);
      System.out.println("---GOT "+segment);
      byte [] buffer = ByteBuffer.allocate(Long.BYTES).putLong(segment).array();
      b += new String(buffer, StandardCharsets.US_ASCII);
      System.out.println("    -------------APPENDING |"+new String(buffer, StandardCharsets.US_ASCII)+"|");
    }
    
    return b;
  }
  
  private static long[] gatherArguments(OperandStack stack, int size) {
    long [] args = new long[size];
    
    for (int i = args.length - 1; i >= 0; i--) {
      args[i] = stack.popOperand();
    }
    
    return args;
  }
}
