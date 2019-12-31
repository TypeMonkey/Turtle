package jg.cs.compile.nodes.atoms;

import net.percederberg.grammatica.parser.Token;

public class Bool extends Atom<Boolean>{

  public Bool(Token boolToken) {
    super(boolToken, Boolean.parseBoolean(boolToken.getImage()));
  }

  @Override
  public String toString() {
    return "BOOL ~ "+getActualValue();
  }
}
