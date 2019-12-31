package jg.cs.compile.nodes.atoms;

import jg.cs.compile.nodes.Expr;
import net.percederberg.grammatica.parser.Token;

public class Int extends Atom<Long>{
    
  public Int(Token intToken) {
    super(intToken, Long.parseLong(intToken.getImage()));
  }
  
  @Override
  public String toString() {
    return "INT ~ "+getActualValue();
  }
}
