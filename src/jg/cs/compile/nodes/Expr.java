package jg.cs.compile.nodes;

import net.percederberg.grammatica.parser.Token;

public abstract class Expr {

  private final Token leadToken;
  
  public Expr(Token leadToken) {
    this.leadToken = leadToken;
  }
  
  public Token getLeadToken() {
    return leadToken;
  }
  
  public int getLeadLnNumber() {
    return leadToken.getStartLine();
  }
  
  public int getLeadColNumber() {
    return leadToken.getStartColumn();
  }
}
