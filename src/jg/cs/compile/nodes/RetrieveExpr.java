package jg.cs.compile.nodes;

import jg.cs.compile.nodes.atoms.Identifier;
import net.percederberg.grammatica.parser.Token;

public class RetrieveExpr extends Expr{

  private final Expr target;
  private final Identifier memberName;
  
  public RetrieveExpr(Token getKeyword, Expr target, Identifier memberName) {
    super(getKeyword);
    this.target = target;
    this.memberName = memberName;
  }

  public Expr getTarget() {
    return target;
  }

  public Identifier getMemberName() {
    return memberName;
  }
}
