package jg.cs.compile.nodes;

import java.util.List;

import net.percederberg.grammatica.parser.Token;

public class FunctionCall extends Expr{
  
  private List<Expr> arguments;
  
  public FunctionCall(Token functionName, List<Expr> arguments) {
    super(functionName);
    this.arguments = arguments;
  }
  
  public List<Expr> getArguments() {
    return arguments;
  }

  public int getArgCount() {
    return arguments.size();
  }

  public Token getFuncName() {
    return getLeadToken();
  }
  
  @Override
  public String toString() {
    return "FCALL ~ "+getFuncName().getImage()+" with "+arguments;
  }
}
