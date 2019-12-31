package jg.cs.compile.nodes.atoms;

import net.percederberg.grammatica.parser.Token;

public class Str extends Atom<String>{

  public Str(Token strToken) {
    super(strToken, strToken.getImage());
  }
  
  public String clipQuotes() {
    if (getActualValue().length() == 2) {
      return "";
    }
    String fstQuote = getActualValue().substring(1);
    return fstQuote.substring(0, fstQuote.length()-1);
  }
  
  @Override
  public String toString() {
    return "STR ~ |"+clipQuotes()+"|";
  }
}
