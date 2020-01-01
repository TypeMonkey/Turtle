package jg.cs.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jg.cs.common.BuiltInFunctions;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.types.Type;
import jg.cs.compile.errors.DuplicateFunctionException;
import jg.cs.compile.errors.MisplacedException;
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
import jg.cs.compile.nodes.atoms.Identifier;
import jg.cs.compile.nodes.atoms.Typ;

/**
 * Checks a program for the following errors: 
 * unbound variables, unbound functions, unbound types,
 * duplicate functions, duplicate struct declarations,
 * and non-top-level struct declarations
 * @author Jose Guaro
 *
 */
public class StructureVerifier {
  private final List<Expr> rawExprs;
  private final Map<FunctionSignature, FunctionLike> topLevelFunctions;
  private final Map<String, DataDeclaration> structDecs;
  private final String filename;
    
  public StructureVerifier(String filename, List<Expr> rawExprs) {
    this.rawExprs = rawExprs;
    this.topLevelFunctions = new HashMap<>();
    this.structDecs = new HashMap<>();
    this.filename = filename;   
  }
  
  public Program verify() {        
    //add all top-level (file) function signatures in the global function map
    //also, add all top level Data declarations in global struct map
    
    //add built-in functions too
    for (Entry<FunctionSignature, FunctionLike> builtin : BuiltInFunctions.BUILT_IN_MAP.entrySet()) {
      topLevelFunctions.put(builtin.getValue().getIdentity().getSignature(), builtin.getValue());
    }
    
    ArrayList<Expr> topLevelStatements = new ArrayList<Expr>();
    for (Expr component : rawExprs) {
      if (component instanceof FunctionLike) {
        FunctionLike exp = (FunctionLike) component;
        
        if (topLevelFunctions.containsKey(exp.getIdentity().getSignature())) {
          throw new DuplicateFunctionException(component.getLeadToken(), exp.getIdentity().getSignature(), filename);
        }
        
        if (exp instanceof DataDeclaration) {
          DataDeclaration declaration = (DataDeclaration) exp;
          structDecs.put(declaration.getName().getImage(), declaration);
        }
        topLevelFunctions.put(exp.getIdentity().getSignature(), exp);
      }
      else {
        topLevelStatements.add(component);
      }
    }
    
    for (Expr component : rawExprs) {
      verifyExpr(component, new ArrayList<>(), fwrap(topLevelFunctions), true);
    }   
    
    Map<Type, DataDeclaration> actualDatas = 
        structDecs.
        entrySet().
        stream().
        collect(Collectors.toMap(x -> Type.createType(x.getKey()), y -> y.getValue()));
    
    return new Program(filename, 
        topLevelFunctions, 
        actualDatas, 
        topLevelStatements);
  }
  
  //VERIFY METHODS DONE------
  
  private void verifyExpr(Expr expr, 
      List<Map<String, IdenTypeValTuple>> others, 
      List<Map<FunctionSignature, FunctionLike>> fenv,
      boolean isTopLevel) {
    //System.out.println("TARGET: "+expr);
    
    /*
     * NOT NEEDED TO CHECK
     * 
    if (expr instanceof Int) {
      continue;
    }
    else if (expr instanceof Bool) {
      continue;
    }
    else if (expr instanceof Str) {
      continue;
    }
    */
    
    /*
     * Guarantees that the parsing stage provides:
     *  - Let expressions with duplicate variables are caught at parsing
     *  - Function definitions with duplicate variables are caught at parsing
     *  - struct/data declarations with duplicate variables are caught at parsing
     */
    
    if (expr instanceof MutateExpr) {
      MutateExpr mutateExpr = (MutateExpr) expr;
      
      //verifying member name will be done during type checking
      
      verifyExpr(mutateExpr.getTarget(), others, fenv, isTopLevel);
      verifyExpr(mutateExpr.getNewValue(), others, fenv, isTopLevel);     
    }
    else if (expr instanceof DataDeclaration) {
      if (!isTopLevel) {
        throw new MisplacedException((FunctDefExpr) expr, filename);
      }
      else {
        /*
         * Data/Structs are invoked in the same manner as functions are.
         * So say we had (data Dat ( a:int b:int c:bool))
         * 
         * To instanstiate a Dat, we do (Dat 2 3 true)
         */
        DataDeclaration declaration = (DataDeclaration) expr;
        for (Entry<Identifier, Typ> entry : declaration.getMembers().entrySet()) {
          if (!structDecs.containsKey(entry.getValue().getActualValue().getName()) && 
              !Type.PRIMITIVE.containsKey(entry.getValue().getActualValue().getName())) {
            throw new UnresolvableComponentException(entry.getValue().getActualValue(), 
                entry.getValue().getLeadToken(),
                filename);
          }
        }
      }
    }
    else if (expr instanceof RetrieveExpr) {
      RetrieveExpr retrieveExpr = (RetrieveExpr) expr;
      
      //verifying member name will be done during type checking
      
      verifyExpr(retrieveExpr.getTarget(), others, fenv, isTopLevel);
    }
    else if (expr instanceof Identifier) {
      verifyIdentifier((Identifier) expr, others, fenv);
    }
    else if (expr instanceof FunctDefExpr) {
      verifyFuncDef((FunctDefExpr) expr, others, fenv);
    }
    else if (expr instanceof LetExpr) {
      verifyLet((LetExpr) expr, others, fenv);
    }
    else if (expr instanceof IfExpr) {
      IfExpr ifExpr = (IfExpr) expr;
      verifyExpr(ifExpr.getCondition(), others, fenv, isTopLevel);
      verifyExpr(ifExpr.getTrueConseq(), others, fenv, isTopLevel);
      verifyExpr(ifExpr.getFalseConseq(), others, fenv, isTopLevel);
    }
    else if (expr instanceof BinaryOpExpr) {
      BinaryOpExpr binExpr = (BinaryOpExpr) expr;
      verifyExpr(binExpr.getLeft(), others, fenv, isTopLevel);
      verifyExpr(binExpr.getRight(), others, fenv, isTopLevel);
    }
    else if (expr instanceof SetExpr) {
      SetExpr setExpr = (SetExpr) expr;
      verifyIdentifier(setExpr.getIdentifier(), others, fenv);
      verifyExpr(setExpr.getValue(), others, fenv, isTopLevel);
    }
    else if (expr instanceof FunctionCall) {
      checkFunctionCall((FunctionCall) expr, others, fenv);
    }
    else if (expr instanceof WhileExpr) {
      checkWhileLoop((WhileExpr) expr, others, fenv);
    }
  }

  private void checkWhileLoop(WhileExpr expr, List<Map<String, IdenTypeValTuple>> env,
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    //verify condition
    verifyExpr(expr.getCondition(), env, fenv, false);
    
    /*
     * there maybe nested functions declared within this while-loop
     * that may have been used. We need a new map to pass to nested expressions for such
     * nested functions
     */
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();
    
    Expr lastExpr = null;
    for (Expr statement : expr.getExpressions()) {
      lastExpr = statement;
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr nested = (FunctDefExpr) statement;
        verifyFuncDef(nested, env, fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(nested.getIdentity().getSignature(), nested);
      }
      else {
        verifyExpr(statement, env, fconcatToFront(localFuncMap, fenv), false);
      }
    }
    
    /*
     * Cannot return functions as value
     */
    if(lastExpr instanceof FunctDefExpr) {
      throw new MisplacedException((FunctDefExpr) lastExpr, filename);
    }
  }

  private void checkFunctionCall(FunctionCall expr, List<Map<String, IdenTypeValTuple>> env,
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    for (Expr arg : expr.getArguments()) {
      verifyExpr(arg, env, fenv, false);
    }
  }

  private void verifyIdentifier(Identifier identifier, List<Map<String, IdenTypeValTuple>> env,
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    for (Map<String, IdenTypeValTuple> map : env) {
      if (map.containsKey(identifier.getActualValue())) {
        return;
      }
    }
    throw new UnresolvableComponentException(identifier, filename);
  }
  
  private void verifyFuncDef(FunctDefExpr expr, List<Map<String, IdenTypeValTuple>> others,
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    /*
     * Guarantees that the parsing stage provides:
     *  - Let expressions with duplicate variables are caught at parsing
     *  - Function definitions with duplicate variables are caught at parsing
     *  - struct/data declarations with duplicate variables are caught at parsing
     */
    
    //build function-local variable env
    LinkedHashMap<String, IdenTypeValTuple> localEnv = new LinkedHashMap<>();
    
    //load all parameters variables. At each parameter, evaluate the type
    //of its value with the current localEnv
    
    for (IdenTypeValTuple var : expr.getParameters().values()) {
      if (!structDecs.containsKey(var.getType().getName()) && 
          !Type.PRIMITIVE.containsKey(var.getType().getName())) {
        throw new UnresolvableComponentException(var.getType(), var.getIdentifier().getLeadToken(), filename);
      }
      localEnv.put(var.getIdentifier().getActualValue(), var);
    }
    
    /*
     * there maybe nested functions declared within this function
     * that may have been used. We need a new map to pass to nested expressions for such
     * nested functions
     */
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();
    
    Expr lastExpr = null;
    for (Expr statement : expr.getExpressionsExprs()) {
      lastExpr = statement;
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr nested = (FunctDefExpr) statement;
        verifyFuncDef(nested, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(nested.getIdentity().getSignature(), nested);
      }
      else {
        verifyExpr(statement, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv), false);
      }
    }
    
    /*
     * Cannot return functions as value
     */
    if(lastExpr instanceof FunctDefExpr) {
      throw new MisplacedException((FunctDefExpr) lastExpr, filename);
    }
  }

  private void verifyLet(LetExpr expr, List<Map<String, IdenTypeValTuple>> others,
      List<Map<FunctionSignature, FunctionLike>> fenv) {
    
    /*
     * Guarantees that the parsing stage provides:
     *  - Let expressions with duplicate variables are caught at parsing
     *  - Function definitions with duplicate variables are caught at parsing
     *  - struct/data declarations with duplicate variables are caught at parsing
     */
    
    //Scope of let expr
    LinkedHashMap<String, IdenTypeValTuple> localEnv = new LinkedHashMap<>();
    for (IdenTypeValTuple var : expr.getVars().values()) {
      if (!structDecs.containsKey(var.getType().getName()) &&
          !Type.PRIMITIVE.containsKey(var.getType().getName())) {
        throw new UnresolvableComponentException(var.getType(), var.getIdentifier().getLeadToken(), filename);
      }
      
      verifyExpr(var.getValue(), concatToFront(localEnv, others) , fenv, false);
      localEnv.put(var.getIdentifier().getActualValue(), var);
    }
    
    /*
     * there maybe nested functions declared within this function
     * that may have been used. We need a new map to pass to nested expressions for such
     * nested functions
     */
    LinkedHashMap<FunctionSignature, FunctionLike> localFuncMap = new LinkedHashMap<>();
    
    Expr lastExpr = null;
    for (Expr statement : expr.getExpressions()) {
      lastExpr = statement;
      if (statement instanceof FunctDefExpr) {
        FunctDefExpr nested = (FunctDefExpr) statement;
        verifyFuncDef(nested, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv));
        localFuncMap.put(nested.getIdentity().getSignature(), nested);
      }
      else {
        verifyExpr(statement, concatToFront(localEnv, others), fconcatToFront(localFuncMap, fenv), false);
      }
    }
    
    /*
     * Cannot return functions as value
     */
    if(lastExpr instanceof FunctDefExpr) {
      throw new MisplacedException((FunctDefExpr) lastExpr, filename);
    }
  }
  //VERIFY METHODS DONE------
  
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
