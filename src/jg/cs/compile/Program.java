package jg.cs.compile;

import java.util.List;
import java.util.Map;

import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.types.Type;
import jg.cs.compile.nodes.DataDeclaration;
import jg.cs.compile.nodes.Expr;

/**
 * Represents a structurally valid Turtle program
 * @author Jose Guaro
 *
 */
public class Program {

  private final String fileName;
  private final Map<FunctionSignature, FunctionLike> fileFunctions; 
  private final Map<Type, DataDeclaration> structDecs;
  private final List<Expr> exprList; //this will include file functions
  
  protected Program(String fileName, 
      Map<FunctionSignature,FunctionLike> fileFunctions,
      Map<Type, DataDeclaration> structDecs,
      List<Expr> exprList) {
      
    this.fileName = fileName;
    this.fileFunctions = fileFunctions;
    this.structDecs = structDecs;
    this.exprList = exprList;
  }
  
  public String getFileName() {
    return fileName;
  }

  public Map<FunctionSignature, FunctionLike> getFileFunctions() {
    return fileFunctions;
  }
  
  public Map<Type, DataDeclaration> getStructDecs() {
    return structDecs;
  }
  
  public boolean typeExists(Type type) {
    return structDecs.containsKey(type) || Type.PRIMITIVE.containsKey(type.getName());
  }

  public List<Expr> getExprList() {
    return exprList;
  }
  
}
