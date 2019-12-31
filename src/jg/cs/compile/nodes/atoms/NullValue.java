package jg.cs.compile.nodes.atoms;

import net.percederberg.grammatica.parser.Token;

public class NullValue extends Atom<Typ>{

  public NullValue(Token sourceToken, Typ nullType) {
    super(sourceToken, nullType);
  }

  @Override
  public String toString() {
    return "NULL ~ "+getLeadToken().getImage()+" at ln: "+getLeadLnNumber();
  }
}
