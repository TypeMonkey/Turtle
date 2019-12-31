package jg.cs.compile.nodes.atoms;

import jg.cs.compile.nodes.Expr;
import net.percederberg.grammatica.parser.Token;

public class Atom<T> extends Expr{

  protected final T actualValue;
  
  public Atom(Token leadToken, T value) {
    super(leadToken);
    this.actualValue = value;
  }
  
  public T getActualValue() {
    return actualValue;
  }

}
