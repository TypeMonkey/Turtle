package jg.cs.compile.nodes;

import jg.cs.compile.nodes.atoms.BinaryOperator;

public class BinaryOpExpr extends Expr{

  private final BinaryOperator operator;
  private final Expr left;
  private final Expr right;
  
  public BinaryOpExpr(BinaryOperator operator, Expr left, Expr right) {
    super(operator.getLeadToken());
    this.operator = operator;
    this.left = left;
    this.right = right;
  } 
  
  public BinaryOperator getOperator() {
    return operator;
  }

  public Expr getLeft() {
    return left;
  }

  public Expr getRight() {
    return right;
  }
  
  @Override
  public String toString() {
    return "BIN ~ "+operator+" [LEFT: "+left+"]  [RIGHT: "+right+"]";
  }
}
