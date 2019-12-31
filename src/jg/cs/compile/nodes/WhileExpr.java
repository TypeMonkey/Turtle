package jg.cs.compile.nodes;

import java.util.List;

import net.percederberg.grammatica.parser.Token;

public class WhileExpr extends Expr{

  private final Expr condition;
  private final List<Expr> expressions;
  
  public WhileExpr(Token whileKeyWord, Expr cond, List<Expr> expressions) {
    super(whileKeyWord);
    this.condition = cond;
    this.expressions = expressions;
  }

  public Expr getCondition() {
    return condition;
  }

  public List<Expr> getExpressions() {
    return expressions;
  }

  @Override
  public String toString() {
    String whole = "WHILE ~ "+condition+System.lineSeparator();
    
    for (Expr expr : expressions) {
      whole += "    "+expr+System.lineSeparator();
    }
    
    return whole;
  }
}
