package jg.cs.compile.nodes;

import jg.cs.compile.nodes.atoms.Identifier;
import net.percederberg.grammatica.parser.Token;

public class SetExpr extends Expr{

  private final Identifier identifier;
  private final Expr value;
  
  public SetExpr(Token setKeyword, Identifier identifier, Expr value) {
    super(setKeyword);
    this.identifier = identifier;
    this.value = value;
  }

  public Identifier getIdentifier() {
    return identifier;
  }

  public Expr getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return "SET ~ "+identifier.getLeadToken().getImage()+" TO "+value;
  }
}
