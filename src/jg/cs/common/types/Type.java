package jg.cs.common.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jg.cs.compile.nodes.atoms.Typ;

public class Type {
  
  public static final Map<String, Type> PRIMITIVE;
  
  public static final Type INTEGER = new Type("int");
  public static final Type BOOLEAN = new Type("bool");
  public static final Type STRING = new Type("string");
  public static final Type VOID = new Type("void");
  
  
  
  static {
    HashMap<String, Type> temp = new HashMap<>();
    
    temp.put("int", INTEGER);
    temp.put("bool", BOOLEAN);
    temp.put("string", STRING);
    temp.put("void", VOID);
    
    PRIMITIVE = Collections.unmodifiableMap(temp);
  }

  
  private final String name;
  
  protected Type(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Type) {
      return name.equals(((Type) obj).name);
    }
    return false;
  }
  
  public int hashCode() {
    return name.hashCode();
  }
  
  public String getName() {
    return name;
  }
 
  public String toString() {
    return name;
  }  
  
  public static final Type createType(String rawType) {
    if (PRIMITIVE.containsKey(rawType)) {
      return PRIMITIVE.get(rawType);
    }
    
    return new Type(rawType);
  }
}
