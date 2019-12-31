package jg.cs.compile.nodes.atoms;

import jg.cs.compile.nodes.Expr;
import net.percederberg.grammatica.parser.Token;

public class Identifier extends Atom<String>{

  public Identifier(Token idenToken) {
    super(idenToken, idenToken.getImage());
  }
  
  @Override
  public int hashCode() {
    return actualValue.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Identifier) {
      Identifier other = (Identifier) obj;
      return other.actualValue.equals(actualValue);
    }
    return false;
  }
  
  @Override
  public String toString() {
    return "IDEN ~ "+getActualValue();
  }

}
