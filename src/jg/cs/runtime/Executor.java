



package jg.cs.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jg.cs.common.BuiltInFunctions;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.OperatorKind;
import jg.cs.common.Type;
import jg.cs.compile.Program;
import jg.cs.compile.errors.UnresolvableComponentException;
import jg.cs.compile.nodes.BinaryOpExpr;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.nodes.FunctDefExpr;
import jg.cs.compile.nodes.FunctionCall;
import jg.cs.compile.nodes.IdenTypeValTuple;
import jg.cs.compile.nodes.IfExpr;
import jg.cs.compile.nodes.LetExpr;
import jg.cs.compile.nodes.SetExpr;
import jg.cs.compile.nodes.WhileExpr;
import jg.cs.compile.nodes.atoms.BinaryOperator;
import jg.cs.compile.nodes.atoms.Bool;
import jg.cs.compile.nodes.atoms.Identifier;
import jg.cs.compile.nodes.atoms.Int;
import jg.cs.compile.nodes.atoms.Str;
import jg.cs.runtime.errors.ExecException;
import jg.cs.runtime.errors.OverflowException;
import jg.cs.runtime.values.BoolValue;
import jg.cs.runtime.values.FunctionValue;
import jg.cs.runtime.values.IntValue;
import jg.cs.runtime.values.StringValue;
import jg.cs.runtime.values.Value;

public class Executor {
  
  private final Program program;
  
  public Executor(Program program) {
    this.program = program;
  }
  
  public Value<?> execute(){
    //System.out.println("---FMAP: "+program.getFileFunctions());
    Value<?> latest = null;
    for (Expr component : program.getExprList()) {
      latest = evalExpr(component, new ArrayList<>(), fwrap(program.getFileFunctions()));
      //System.out.println("**************************");
      //System.out.println("******----------**********");
    }   
    
    return latest;
  }
  
  //EVAL METHODS Begin
  
  private Value evalExpr(Expr expr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctionLike>> fenv) throws ExecException{
    //System.out.println("TARGET: "+expr);
    if (expr instanceof Int) {
      //System.out.println(" FOR INT "+((Int) expr).getActualValue());
      return new IntValue(((Int) expr).getActualValue());
    }
    else if (expr instanceof Bool) {
      //System.out.println(" FOR BOOL "+((Bool) expr).getActualValue());
      return new BoolValue(((Bool) expr).getActualValue());
    }
    else if (expr instanceof Str) {
      //System.out.println(" FOR STR '"+((Str) expr).clipQuotes()+"'");
      return new StringValue(((Str) expr).clipQuotes());
    }
    else if (expr instanceof Identifier) {
      return evalIdentifier((Identifier) expr, env, fenv);
    }
    else if (expr instanceof FunctDefExpr) {
      FunctDefExpr functDef = (FunctDefExpr) expr;
      return evalFunctionDef(functDef, env, fenv);
    }
    else if (expr instanceof LetExpr) {
      return evalLet((LetExpr) expr, env, fenv);
    }
    else if (expr instanceof IfExpr) {
      return evalIf((IfExpr) expr, env, fenv);
    }
    else if (expr instanceof BinaryOpExpr) {
      return evalBinaryOp((BinaryOpExpr) expr, env, fenv);
    }
    else if (expr instanceof SetExpr) {
      return evalSetExpr((SetExpr) expr, env, fenv);
    }
    else if (expr instanceof FunctionCall) {
      return evalFunctionCall((FunctionCall) expr, env, fenv);
    }
    else if (expr instanceof WhileExpr) {
      return evalWhileLoop((WhileExpr) expr, env, fenv);
    }
    
    //System.out.println("unknown? "+expr.getClass());
    return null;
  }
  
  private Value<?> evalBinaryOp(BinaryOpExpr binaryOpExpr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    OperatorKind operatorKind = binaryOpExpr.getOperator().getActualValue();
    
    Value<?> leftValue = evalExpr(binaryOpExpr.getLeft(), env, fenv);
    Value<?> rightValue = evalExpr(binaryOpExpr.getRight(), env, fenv);
    
    //System.out.println(" FOR BIN-OP: left = "+leftValue+" , right = "+rightValue);
    //System.out.println("    LEFT: "+binaryOpExpr.getLeft());
    //System.out.println("    RIGHT: "+binaryOpExpr.getRight());
    
    if (operatorKind == OperatorKind.EQUAL) {
      return new BoolValue(leftValue.getActualValue().equals(rightValue.getActualValue()));
    }
    else if (operatorKind == OperatorKind.PLUS) {
      if (leftValue.getValueType() == Type.STRING || rightValue.getValueType() == Type.STRING) {
        return new StringValue(leftValue.getActualValue().toString() + rightValue.getActualValue().toString());
      }
      else {
        IntValue intLeft = (IntValue) leftValue;
        IntValue intRight = (IntValue) rightValue;

        try {
          long sum = Math.addExact(intLeft.getActualValue(), intRight.getActualValue());
          return new IntValue(sum);
        } catch (Exception e) {
          throw new OverflowException(binaryOpExpr.getOperator(), program.getFileName());
        }         
      }
    }
    else {
      IntValue intLeft = (IntValue) leftValue;
      IntValue intRight = (IntValue) rightValue;

      if (operatorKind == OperatorKind.MINUS) {
        try {
          long sum = Math.subtractExact(intLeft.getActualValue(), intRight.getActualValue());
          return new IntValue(sum);
        } catch (Exception e) {
          throw new OverflowException(binaryOpExpr.getOperator(), program.getFileName());
        }
      }
      else if (operatorKind == OperatorKind.TIMES) {
        try {
          long sum = Math.multiplyExact(intLeft.getActualValue(), intRight.getActualValue());
          return new IntValue(sum);
        } catch (Exception e) {
          throw new OverflowException(binaryOpExpr.getOperator(), program.getFileName());
        }
      }
      else if (operatorKind == OperatorKind.LESS) {
        return new BoolValue(intLeft.getActualValue() < intRight.getActualValue());
      }
      else if (operatorKind == OperatorKind.GREAT) {
        return new BoolValue(intLeft.getActualValue() > intRight.getActualValue());
      }
      
      throw new RuntimeException("Unknown operator: "+operatorKind);
    }
  }
  
  private Value<?> evalFunctionCall(FunctionCall call, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {

    //System.out.println("****** FUN CALL "+call);
    Type [] argTypes = new Type[call.getArgCount()];
    Value<?> [] argValue = new Value<?>[call.getArgCount()];
    int i = 0;
    for(Expr argument : call.getArguments()) {
      argValue[i] = evalExpr(argument, env, fenv);
      argTypes[i] = argValue[i].getValueType();
      //System.out.println("    ---> ARG["+i+"]:  "+argument);
      i++;
    }

    FunctionSignature signature = new FunctionSignature(
        call.getFuncName().getImage(), 
        argTypes);

    FunctDefExpr foundFunction = null;
    for (Map<FunctionSignature, FunctDefExpr> fmap : fenv) {
      if (fmap.containsKey(signature)) {
        foundFunction = fmap.get(signature);
      }
    }

    
    //if function isn't found, then check built ins
    if (foundFunction == null && BuiltInFunctions.BUILT_IN_MAP.containsKey(signature)) {
      return BuiltInFuncEvaluator.callBuiltIn(BuiltInFunctions.BUILT_IN_MAP.get(signature), argValue);
    }
    else {
      LinkedHashMap<String, Value<?>> argMap = new LinkedHashMap<>();
      i = 0;
      for (Entry<String, IdenTypeValTuple> param: foundFunction.getParameters().entrySet()) {
        argMap.put(param.getKey(), argValue[i]);
        i++;
      }  

      Value<?> latest = null;
      LinkedHashMap<FunctionSignature, FunctDefExpr> localFuncMap = new LinkedHashMap<>();

      //System.out.println("---ARG MAP: "+argMap+" | "+fenv);
      
      for(Expr statement : foundFunction.getExpressionsExprs()) {
        if (statement instanceof FunctDefExpr) {
          FunctDefExpr functDefExpr = (FunctDefExpr) statement;
          latest = evalFunctionDef(functDefExpr, env, fconcatToFront(localFuncMap, fenv));
          localFuncMap.put(functDefExpr.getIdentity().getSignature(), functDefExpr);
        }
        else {
          latest = evalExpr(statement, concatToFront(argMap, env), fconcatToFront(localFuncMap, fenv));
        }
      }

      return latest;
    }
  }
  
  private Value<?> evalIf(IfExpr ifExpr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    //System.out.println(" --- IF CODE: "+ifExpr.getCondition()+" | "+env);
    
    //check condition
    BoolValue condition = (BoolValue) evalExpr(ifExpr.getCondition(), env, fenv);
    
    //System.out.println("---- FOR IF: "+ifExpr.getCondition()+"  got "+condition);
    if (condition.getActualValue()) {
      return evalExpr(ifExpr.getTrueConseq(), env, fenv);
    }
    else {
      return evalExpr(ifExpr.getFalseConseq(), env, fenv);
    }    
  }
  
  private Value<?> evalWhileLoop(WhileExpr whileExpr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    //check condition prior to iterating
    Value<?> latest = (BoolValue) evalExpr(whileExpr.getCondition(), env, fenv);    
    BoolValue condition = (BoolValue) latest;
    
    //System.out.println("  FOR WHILE LOOP: "+whileExpr);
    
    while (condition.getActualValue()) {
      LinkedHashMap<FunctionSignature, FunctDefExpr> localFuncMap = new LinkedHashMap<>();
      
      for(Expr statement : whileExpr.getExpressions()) {
        //System.out.println("   PRIOR ENV MAP: "+env);
        if (statement instanceof FunctDefExpr) {
          FunctDefExpr functDefExpr = (FunctDefExpr) statement;
          latest = evalFunctionDef(functDefExpr, env, fconcatToFront(localFuncMap, fenv));
          localFuncMap.put(functDefExpr.getIdentity().getSignature(), functDefExpr);
        }
        else {
          latest = evalExpr(statement, env, fconcatToFront(localFuncMap, fenv));
        }
        //System.out.println("   AFTER ENV MAP: "+env);
      }
      
      condition = (BoolValue) evalExpr(whileExpr.getCondition(), env , fenv);
    }
    
    return latest;
  }
  
  private Value<?> evalLet(LetExpr expr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    
    LinkedHashMap<String, Value<?>> localEnv = new LinkedHashMap<>();
   
    //System.out.println("---LET: "+expr.getExpressions());
    
    //load all local variables with their initialized variable
    Map<String, IdenTypeValTuple> originalMap = expr.getVars();
    for (Entry<String, IdenTypeValTuple> entry : originalMap.entrySet()) {
      Value<?> initValue = evalExpr(entry.getValue().getValue(), env, fenv);
      localEnv.put(entry.getKey(), initValue);
    }
    
    //System.out.println("----DONE CHECKING LET "+localEnv);
    //now, check all statements
    Value<?> latestValue = null;
    
    LinkedHashMap<FunctionSignature, FunctDefExpr> localFuncMap = new LinkedHashMap<>();
    
    for(Expr statement : expr.getExpressions()) {
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr functDefExpr = (FunctDefExpr) statement;
        latestValue = evalFunctionDef(functDefExpr, concatToFront(localEnv, env), fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(functDefExpr.getIdentity().getSignature(), functDefExpr);
      }
      else {
        latestValue = evalExpr(statement, concatToFront(localEnv, env), fconcatToFront(localFuncMap, fenv));
      }
    }
    
    //System.out.println("-----AFTER LET: "+localEnv);
    
    return latestValue;
  }
  
  private FunctionValue evalFunctionDef(FunctDefExpr expr, 
      List<Map<String, Value<?>>> others, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    return new FunctionValue(expr);
  }
  
  private Value<?> evalSetExpr(SetExpr setExpr, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    
    Identifier identifier = setExpr.getIdentifier();
    Value<?> value = evalExpr(setExpr.getValue(), env, fenv);
    
    for (Map<String, Value<?>> map : env) {
      if (map.containsKey(identifier.getActualValue())) {
        map.put(identifier.getActualValue(), value);
      }
    }
    
    return value;
  }
  
  private Value<?> evalIdentifier(Identifier identifier, 
      List<Map<String, Value<?>>> env, 
      List<Map<FunctionSignature, FunctDefExpr>> fenv) {
    for (Map<String, Value<?>> map : env) {
      if (map.containsKey(identifier.getActualValue())) {
        //System.out.println(" FOR VAR '"+identifier.getActualValue()+"' , the value found is "+map.get(identifier.getActualValue())+" | "+env);
        return map.get(identifier.getActualValue());
      }
    }
    throw new UnresolvableComponentException(identifier, program.getFileName());
  }
  //EVAL METHODS DONE
  
  private List<Map<String, IdenTypeValTuple>> concatToFront(
      Map<String, IdenTypeValTuple> newMap, 
      List<Map<String, IdenTypeValTuple>> others){
    ArrayList<Map<String, IdenTypeValTuple>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    newEnv.addAll(others);
    
    return newEnv;
  }
  

  private List<Map<FunctionSignature, FunctionLike>> fconcatToFront(
      Map<FunctionSignature, FunctionLike> newMap, 
      List<Map<FunctionSignature, FunctionLike>> others){
    ArrayList<Map<FunctionSignature, FunctionLike>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    newEnv.addAll(others);
    
    return newEnv;
  }

  private List<Map<FunctionSignature, FunctionLike>> fwrap(Map<FunctionSignature, FunctionLike> newMap){
    ArrayList<Map<FunctionSignature, FunctionLike>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    
    return newEnv;
  }
  
}
