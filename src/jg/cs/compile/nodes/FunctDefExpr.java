package jg.cs.compile.nodes;

import java.util.LinkedHashMap;
import java.util.List;

import jg.cs.common.FunctionIdentity;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.types.FunctionType;
import jg.cs.common.types.Type;
import jg.cs.compile.nodes.atoms.Typ;
import net.percederberg.grammatica.parser.Token;

public class FunctDefExpr extends Expr implements FunctionLike{

  private final Token funcName;
  private final Typ returnType;
  private final LinkedHashMap<String, IdenTypeValTuple> parameters;
  private final List<Expr> expressionsExprs;
  
  private final FunctionIdentity identity;
  private final FunctionType type;
  
  public FunctDefExpr(Token defKeyword, 
      Token funcName, 
      LinkedHashMap<String, IdenTypeValTuple> parameters, 
      Typ returnType,
      List<Expr> expressions) {
    super(defKeyword);
    this.funcName = funcName;
    this.returnType = returnType;
    this.parameters = parameters;
    this.expressionsExprs = expressions;
    
    identity = createIdentity();
    type = new FunctionType(identity);
  }
  
  private FunctionIdentity createIdentity() {
    Type [] paramTypes = new Type[parameters.size()];
    int i = 0;
    for (IdenTypeValTuple tuple : parameters.values()) {
      paramTypes[i] = tuple.getType();
      i++;
    }
    
    FunctionSignature signature = new FunctionSignature(funcName.getImage(), paramTypes);
    return new FunctionIdentity(signature, returnType.getActualValue());
  }

  public Token getFuncName() {
    return funcName;
  }

  public Typ getReturnType() {
    return returnType;
  }

  public LinkedHashMap<String, IdenTypeValTuple> getParameters() {
    return parameters;
  }
  
  public int getParameterCount() {
    return parameters.size();
  }

  public List<Expr> getExpressionsExprs() {
    return expressionsExprs;
  }
  
  public FunctionIdentity getIdentity() {
    return identity;
  }
  
  public FunctionType getType() {
    return type;
  }
  
  public String toString() {
    String whole = "FUNC ~ "+funcName.getImage()+" "+parameters+" , RET: "+returnType.getActualValue()+System.lineSeparator();
    
    int stateCount = 1;
    for (Expr expr : expressionsExprs) {
      whole += "   "+stateCount+": "+expr+System.lineSeparator();
      stateCount++;
    }
    
    return whole;
  }

  @Override
  public boolean isBuiltIn() {
    return false;
  }
}
