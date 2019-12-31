package jg.cs.compile.nodes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.percederberg.grammatica.parser.Token;

public class LetExpr extends Expr{
  
  private final LinkedHashMap<String, IdenTypeValTuple> vars;
  private final List<Expr> expressions;
  
  public LetExpr(Token letKeyWord , 
      LinkedHashMap<String, IdenTypeValTuple> vars,
      List<Expr> expressions) {
    super(letKeyWord);
    this.vars = vars;
    this.expressions = expressions;
  }
  
  public Map<String, IdenTypeValTuple> getVars() {
    return vars;
  }

  public List<Expr> getExpressions() {
    return expressions;
  }
  
  @Override
  public String toString() {
    String whole = "LET ~ "+vars+System.lineSeparator();
    
    for (Expr expr : expressions) {
      whole += "   "+expr+System.lineSeparator();
    }
    
    return whole;
  }
}
