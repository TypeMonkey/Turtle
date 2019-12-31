package jg.cs.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jg.cs.common.BuiltInFunctions;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.OperatorKind;
import jg.cs.common.types.Type;
import jg.cs.compile.errors.TypeMismatchException;
import jg.cs.compile.errors.UnresolvableComponentException;
import jg.cs.compile.nodes.BinaryOpExpr;
import jg.cs.compile.nodes.DataDeclaration;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.nodes.FunctDefExpr;
import jg.cs.compile.nodes.FunctionCall;
import jg.cs.compile.nodes.IdenTypeValTuple;
import jg.cs.compile.nodes.IfExpr;
import jg.cs.compile.nodes.LetExpr;
import jg.cs.compile.nodes.MutateExpr;
import jg.cs.compile.nodes.RetrieveExpr;
import jg.cs.compile.nodes.SetExpr;
import jg.cs.compile.nodes.WhileExpr;
import jg.cs.compile.nodes.atoms.BinaryOperator;
import jg.cs.compile.nodes.atoms.Bool;
import jg.cs.compile.nodes.atoms.Identifier;
import jg.cs.compile.nodes.atoms.Int;
import jg.cs.compile.nodes.atoms.NullValue;
import jg.cs.compile.nodes.atoms.Str;
import jg.cs.compile.nodes.atoms.Typ;

public class TypeChecker {

  private final Program program;
  private final Set<FunctionSignature> checkedFunctions;
  
  public TypeChecker(Program program) {
    this.program = program;
    this.checkedFunctions = new HashSet<>();
  }
  
  public Type checkType() {    
    //System.out.println("---FMAP: "+program.getFileFunctions());
    Type latest = null;
    for (Expr component : program.getExprList()) {
      latest = checkExpr(component, new ArrayList<>(), fwrap(program.getFileFunctions()));
    }   
    
    return latest;
  }
  
  private Type checkExpr(Expr expr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    //System.out.println("TARGET: "+expr);
    if (expr instanceof Int) {
      return Type.INTEGER;
    }
    else if (expr instanceof Bool) {
      return Type.BOOLEAN;
    }
    else if (expr instanceof Str) {
      return Type.STRING;
    }
    else if (expr instanceof NullValue) {
      return ((NullValue) expr).getActualValue().getActualValue();
    }
    else if (expr instanceof Identifier) {
      return checkIdentifier((Identifier) expr, others, fenv);
    }
    else if (expr instanceof FunctDefExpr) {
      FunctDefExpr functDef = (FunctDefExpr) expr;
      //System.out.println("--------CHECKED: "+checkedFunctions);
      if (checkedFunctions.contains(functDef.getIdentity().getSignature())) {
        return functDef.getReturnType().getActualValue();
      }
      return checkFuncDef(functDef, others, fenv);
    }
    else if (expr instanceof LetExpr) {
      return checkLet((LetExpr) expr, others, fenv);
    }
    else if (expr instanceof IfExpr) {
      return checkIf((IfExpr) expr, others, fenv);
    }
    else if (expr instanceof BinaryOpExpr) {
      return checkBinaryOperation((BinaryOpExpr) expr, others, fenv);
    }
    else if (expr instanceof SetExpr) {
      return checkSetExpr((SetExpr) expr, others, fenv);
    }
    else if (expr instanceof FunctionCall) {
      return checkFunctionCall((FunctionCall) expr, others, fenv);
    }
    else if (expr instanceof WhileExpr) {
      return checkWhileLoop((WhileExpr) expr, others, fenv);
    }
    else if (expr instanceof MutateExpr) {
      MutateExpr mutateExpr = (MutateExpr) expr;

      Type targetType = checkExpr(mutateExpr.getTarget(), others, fenv);
      Type newValue = checkExpr(mutateExpr.getNewValue(), others, fenv);

      Identifier memberName = mutateExpr.getMemberName();

      DataDeclaration targetDeclaration = program.getStructDecs().get(targetType);
      Typ decType = targetDeclaration.getMembers().get(memberName);
      if (decType != null) {
        //target of mutation exists
        if (!decType.getActualValue().equals(newValue)) {
          throw new TypeMismatchException(mutateExpr.getTarget().getLeadToken(), 
              decType.getActualValue(), 
              newValue, 
              program.getFileName());
        }
        else {
          return targetDeclaration.getIdentity().getReturnType();
        }
      }
      else {
        //throw unbound error
        throw new UnresolvableComponentException(memberName, targetDeclaration, program.getFileName());
      }
      
    }
    else if (expr instanceof DataDeclaration) {
      DataDeclaration dataDec = (DataDeclaration) expr;
      return dataDec.getIdentity().getReturnType();
    }
    else if (expr instanceof RetrieveExpr) {
      RetrieveExpr retrieveExpr = (RetrieveExpr) expr;
      
      Type target = checkExpr(retrieveExpr.getTarget(), others, fenv);
      DataDeclaration declaration = program.getStructDecs().get(target);
      Typ decType = declaration.getMembers().get(retrieveExpr.getMemberName());
      if (decType != null) {
        //target of mutation exists
        return decType.getActualValue();
      }
      else {
        //throw unbound error
        throw new UnresolvableComponentException(retrieveExpr.getMemberName(), declaration, program.getFileName());
      }
      
    }
    
    //System.out.println("unknown? "+expr.getClass());
    return null;
  }
  
  private Type checkBinaryOperation(BinaryOpExpr binaryOpExpr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    //System.out.println("----BIN OP: "+binaryOpExpr);
    Type leftType = checkExpr(binaryOpExpr.getLeft(), others, fenv);
    Type rightType = checkExpr(binaryOpExpr.getRight(), others, fenv);
    
    BinaryOperator operator = binaryOpExpr.getOperator();
    OperatorKind operatorKind = operator.getActualValue();
    
    if (operatorKind == OperatorKind.EQUAL) {
      return Type.BOOLEAN;
    }
    else if (operatorKind == OperatorKind.PLUS) {
      if (leftType.equals(Type.INTEGER) && rightType.equals(Type.INTEGER)) {
        return Type.INTEGER;
      }
      else if (leftType == Type.STRING || rightType == Type.STRING) {
        return Type.STRING;
      }
      else {
        throw new TypeMismatchException(operatorKind, 
            binaryOpExpr.getLeft(), leftType, Type.INTEGER, program.getFileName());
      }
    }
    else {
      //arithmetic operators
      HashSet<OperatorKind> arithOps = new HashSet<OperatorKind>();
      arithOps.addAll(Arrays.asList(OperatorKind.MINUS, OperatorKind.TIMES, OperatorKind.EXP));
      
      if (arithOps.contains(operatorKind)) {
        if (!leftType.equals(Type.INTEGER)) {
          //System.out.println("----LEFT TYPE: "+leftType);
          throw new TypeMismatchException(operatorKind, 
              binaryOpExpr.getLeft(), leftType, Type.INTEGER, program.getFileName());
        }
        else if (!rightType.equals(Type.INTEGER)) {
          throw new TypeMismatchException(operatorKind, 
              binaryOpExpr.getRight(), rightType, Type.INTEGER, program.getFileName());
        }
        else {
          return Type.INTEGER;
        }
      }
      else {
        //comparison operator
        if (!leftType.equals(Type.INTEGER)) {
          throw new TypeMismatchException(operatorKind, 
              binaryOpExpr.getLeft(), leftType, Type.INTEGER, program.getFileName());
        }
        else if (!rightType.equals(Type.INTEGER)) {
          throw new TypeMismatchException(operatorKind, 
              binaryOpExpr.getRight(), rightType, Type.INTEGER, program.getFileName());
        }
        else {
          return Type.BOOLEAN;
        }
      }
    }
  }
  
  private Type checkFunctionCall(FunctionCall call, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    //System.out.println("****** FUN CALL "+call);
    Type [] argTypes = new Type[call.getArgCount()];
    int i = 0;
    for(Expr argument : call.getArguments()) {
      argTypes[i] = checkExpr(argument, others, fenv);
      //System.out.println("    ---> ARG["+i+"]:  "+argument);
      i++;
    }
    
    FunctionSignature signature = new FunctionSignature(
        call.getFuncName().getImage(), 
        argTypes);
    
    for (Map<FunctionSignature, FunctionLike> fmap : fenv) {
      if (fmap.containsKey(signature)) {
        Type retType = fmap.get(signature).getIdentity().getReturnType();
        //System.out.println(" --->!!! FOUND TYPE: "+retType);
        
        return retType;
      }
    }
    
    //if function isn't found, then check built ins
    if (BuiltInFunctions.BUILT_IN_MAP.containsKey(signature)) {
      return BuiltInFunctions.BUILT_IN_MAP.get(signature).getIdentity().getReturnType();
    }
    
    //System.out.println("FINDING: "+signature);
    //System.out.println("  MAPS: "+fenv);
    throw new UnresolvableComponentException(signature, 
        call.getLeadToken(), 
        program.getFileName());
  }
  
  private Type checkIf(IfExpr ifExpr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    Type actualTypeOfCond = checkExpr(ifExpr.getCondition(), others, fenv);
    if (!actualTypeOfCond.equals(Type.BOOLEAN)) {
      throw new TypeMismatchException(ifExpr.getLeadToken(), 
          Type.BOOLEAN, actualTypeOfCond, program.getFileName());
    }
    
    //check if both consequences have the same type
    Type trueConseq = checkExpr(ifExpr.getTrueConseq(), others, fenv);
    Type falseConseq = checkExpr(ifExpr.getFalseConseq(), others, fenv);
    
    if (!trueConseq.equals(falseConseq)) {
      throw new TypeMismatchException(ifExpr.getLeadToken(), 
          trueConseq, 
          falseConseq, 
          program.getFileName());
    }
    
    return trueConseq;
  }
  
  private Type checkWhileLoop(WhileExpr whileExpr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    Type actualTypeOfCond = checkExpr(whileExpr.getCondition(), others, fenv);
    if (!actualTypeOfCond .equals(Type.BOOLEAN)) {
      throw new TypeMismatchException(whileExpr.getLeadToken(), 
          Type.BOOLEAN, actualTypeOfCond, program.getFileName());
    }
    
    //now, check all statements
    Type latestType = null;
    
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();   
    
    for(Expr statement : whileExpr.getExpressions()) {
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr functDefExpr = (FunctDefExpr) statement;
        latestType = checkFuncDef(functDefExpr, others, fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(functDefExpr.getIdentity().getSignature(), functDefExpr);
      }
      else {
        latestType = checkExpr(statement, others, fenv);
      }
    }
    
    return latestType;
  }
  
  private Type checkLet(LetExpr expr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    LinkedHashMap<String, IdenTypeValTuple> localEnv = new LinkedHashMap<>();
   
    //System.out.println("---LET: "+expr.getExpressions());
    
    //load all local variables. At each local variable, evaluate the type
    //of its value with the current localEnv
    
    for (IdenTypeValTuple var : expr.getVars().values()) {
      Type typeOfValue = checkExpr(var.getValue(), concatToFront(localEnv, others) , fenv);
      if (!var.getType().equals(typeOfValue)) {
        throw new TypeMismatchException(var, typeOfValue, program.getFileName());
      }
      
      localEnv.put(var.getIdentifier().getActualValue(), var);
    }
    
    //System.out.println("----DONE CHECKING LET");
    //now, check all statements
    Type latestType = null;
    
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();
    
    
    for(Expr statement : expr.getExpressions()) {
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr functDefExpr = (FunctDefExpr) statement;
        latestType = checkFuncDef(functDefExpr, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(functDefExpr.getIdentity().getSignature(), functDefExpr);
      }
      else {
        latestType = checkExpr(statement, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
      }
    }
    
    
    return latestType;
  }
  
  private Type checkFuncDef(FunctDefExpr expr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    LinkedHashMap<String, IdenTypeValTuple> localEnv = new LinkedHashMap<>();
   
    //load all parameters variables. At each parameter, evaluate the type
    //of its value with the current localEnv
    
    for (IdenTypeValTuple var : expr.getParameters().values()) {
      localEnv.put(var.getIdentifier().getActualValue(), var);
    }
    
    //now, check all statements   
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();
    
    Type lastType = Type.VOID; //in case the function is empty
        
    for(Expr statement : expr.getExpressionsExprs()) {
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr latestFuncDef = (FunctDefExpr) statement;
        localFuncMap.put(latestFuncDef.getIdentity().getSignature(), latestFuncDef);
        lastType = checkFuncDef(latestFuncDef, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
      }
      else {
        lastType = checkExpr(statement, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
      }
    }
    
    if (!lastType.equals(expr.getReturnType().getActualValue()) && 
        !expr.getReturnType().getActualValue().equals(Type.VOID)) {
      throw new TypeMismatchException(expr.getLeadToken(),
          expr.getReturnType().getActualValue(), lastType, program.getFileName());
    }
    
    return expr.getType();   
  }
  
  private Type checkSetExpr(SetExpr setExpr, 
      List<Map<String, IdenTypeValTuple>> env, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    Type varType = checkIdentifier(setExpr.getIdentifier(), env, fenv);
    
    Type valueType = checkExpr(setExpr.getValue(), env, fenv);
    
    if (varType.equals(valueType)) {
      return varType;
    }
    throw new TypeMismatchException(setExpr.getLeadToken(), 
        varType, 
        valueType, 
        program.getFileName());
  }
  
  private Type checkIdentifier(Identifier identifier, 
      List<Map<String, IdenTypeValTuple>> env, 
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    for (Map<String, IdenTypeValTuple> map : env) {
      if (map.containsKey(identifier.getActualValue())) {
        return map.get(identifier.getActualValue()).getType();
      }
    }
    throw new UnresolvableComponentException(identifier, program.getFileName());
  }
  
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
