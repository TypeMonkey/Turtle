package jg.cs.compile.nodes;

import net.percederberg.grammatica.parser.Token;

public class IfExpr extends Expr{

  private final Expr condition;
  private final Expr trueConseq;
  private final Expr falseConseq;
  
  public IfExpr(Token ifKeyWord, Expr condition, Expr trueConseq, Expr falseConseq) {
    super(ifKeyWord);
    this.condition = condition;
    this.trueConseq = trueConseq;
    this.falseConseq = falseConseq;
  }

  public Expr getCondition() {
    return condition;
  }

  public Expr getTrueConseq() {
    return trueConseq;
  }

  public Expr getFalseConseq() {
    return falseConseq;
  }
  
  @Override
  public String toString() {
    return "IF ~ "+condition+" THEN "+trueConseq+" ELSE "+falseConseq;
  }
}
