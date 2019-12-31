package jg.cs.runtime;

import java.util.Scanner;

import jg.cs.common.BuiltInFunctions;
import jg.cs.common.types.Type;
import jg.cs.runtime.values.BoolValue;
import jg.cs.runtime.values.IntValue;
import jg.cs.runtime.values.StringValue;
import jg.cs.runtime.values.Value;

public final class BuiltInFuncEvaluator {
  
  public static Value<?> callBuiltIn(BuiltInFunctions function, Value<?> ... args){
    if (function == BuiltInFunctions.INPUT) {
      return input();
    }
    else if (function == BuiltInFunctions.PRINT) {
      return print((StringValue) args[0]);
    }
    else if (function == BuiltInFunctions.PRINTLN) {
      return println((StringValue) args[0]);
    }
    else if (function == BuiltInFunctions.INC) {
      return inc((IntValue) args[0]);
    }
    else if (function == BuiltInFunctions.DEC) {
      return dec((IntValue) args[0]);
    }
    else if (function == BuiltInFunctions.LEN) {
      return len((StringValue) args[0]);
    }
    else if(function.getIdentity().getSignature().getName().equals("isInt")){
      return isInt(args[0]);
    }
    else if (function.getIdentity().getSignature().getName().equals("isBool")) {
      return isBool(args[0]);
    }
    else if (function.getIdentity().getSignature().getName().equals("isStr")) {
      return isStr(args[0]);
    }
    else if (function.getIdentity().getSignature().getName().equals("toStr")) {
      return toStr(args[0]);
    }
    else {
      return type(args[0]);
    }
  }
  
  public static IntValue len(StringValue value) {
    return new IntValue((long) value.getActualValue().length());
  }
  
  public static StringValue input() {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    scanner.close();
    return new StringValue(input);
  }
  
  public static StringValue print(StringValue value) {
    System.out.print(value.getActualValue());
    return value;
  }
  
  public static StringValue println(StringValue value) {
    System.out.println(value.getActualValue());
    return value;
  }
  
  public static IntValue inc(IntValue value) {
    return new IntValue(value.getActualValue() + 1);
  }
  
  public static IntValue dec(IntValue value) {
    return new IntValue(value.getActualValue() - 1);
  }
  
  public static StringValue toStr(Value<?> value) {
    return new StringValue(value.getActualValue().toString());
  }
  
  public static BoolValue isInt(Value<?> value) {
    return new BoolValue(value.getValueType() == Type.INTEGER);
  }
  
  public static BoolValue isStr(Value<?> value) {
    return new BoolValue(value.getValueType() == Type.STRING);
  }
  
  public static BoolValue isBool(Value<?> value) {
    return new BoolValue(value.getValueType() == Type.BOOLEAN);
  }
  
  public static StringValue type(Value<?> value) {
    return new StringValue(value.getValueType().getName());
  }
}
