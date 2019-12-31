package jg.cs.compile.nodes.atoms;

import net.percederberg.grammatica.parser.Token;

/**
 * A placeholder Atom for parsing purposes
 * @author gauro
 *
 */
public class Keyword extends Atom<String>{

  public Keyword(Token keyword) {
    super(keyword, keyword.getImage());
  }
  
  @Override
  public String toString() {
    return "KW ~ "+getActualValue();
  }
}
