package jg.cs.compile.nodes;

import java.util.LinkedHashMap;

import jg.cs.common.FunctionIdentity;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.types.Type;
import jg.cs.compile.nodes.atoms.Identifier;
import jg.cs.compile.nodes.atoms.Typ;
import net.percederberg.grammatica.parser.Token;

public class DataDeclaration extends Expr implements FunctionLike{
  
  private final Token name;
  private final LinkedHashMap<Identifier, Typ> members;
  
  private final FunctionIdentity identity;

  public DataDeclaration(Token dataKeyWord, Token name, LinkedHashMap<Identifier, Typ> members) {
    super(dataKeyWord);
    this.name = name;
    this.members = members;
    this.identity = createIdentity();
  } 

  private FunctionIdentity createIdentity() {
    Type [] parameters = members.values().stream().map(x -> x.getActualValue()).toArray(Type[]::new);
    
    FunctionSignature signature = new FunctionSignature(name.getImage(), parameters);
    
    FunctionIdentity identity = new FunctionIdentity(signature, Type.createType(name.getImage()));
    
    return identity;
  }
  
  public FunctionIdentity getIdentity() {
    return identity;
  }

  public Token getName() {
    return name;
  }

  public LinkedHashMap<Identifier, Typ> getMembers() {
    return members;
  }

  @Override
  public boolean isBuiltIn() {
    return false;
  }
}
