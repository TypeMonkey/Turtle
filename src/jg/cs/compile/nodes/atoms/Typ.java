package jg.cs.compile.nodes.atoms;

import jg.cs.common.types.Type;
import net.percederberg.grammatica.parser.Token;

public class Typ extends Atom<Type>{
  
  public Typ(Token typeToken) {
    super(typeToken, Type.createType(typeToken.getImage()));
  }

  @Override
  public String toString() {
    return "TYPE ~ "+getActualValue().getName();
  }
}
