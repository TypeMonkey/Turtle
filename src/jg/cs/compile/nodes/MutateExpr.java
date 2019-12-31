package jg.cs.compile.nodes;

import jg.cs.compile.nodes.atoms.Identifier;
import net.percederberg.grammatica.parser.Token;

public class MutateExpr extends Expr{

  private final Expr target;
  private final Identifier memberName;
  private final Expr newValue;
  
  public MutateExpr(Token mutateKeyword, Expr target, Identifier memberName, Expr newValue) {
    super(mutateKeyword);
    this.target = target;
    this.memberName = memberName;
    this.newValue = newValue;
  }

  public Expr getTarget() {
    return target;
  }

  public Identifier getMemberName() {
    return memberName;
  }

  public Expr getNewValue() {
    return newValue;
  }
}
