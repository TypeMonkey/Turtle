package jg.cs.compile.nodes;

import jg.cs.common.types.Type;
import jg.cs.compile.nodes.atoms.Identifier;

public class IdenTypeValTuple {
  private final Identifier identifier;
  private final Type type;
  private final Expr value;

  public IdenTypeValTuple(Identifier identifier, Type type) {
    this(identifier, type, null);
  }
  
  public IdenTypeValTuple(Identifier identifier, Type type, Expr value) {
    this.identifier = identifier;
    this.type = type;
    this.value = value;
  }

  public Identifier getIdentifier() {
    return identifier;
  }

  public Type getType() {
    return type;
  }
  
  public Expr getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "ID ~ "+identifier.getActualValue()+" of "+type+(value == null ? "" : " , val: "+value);
  }
}
