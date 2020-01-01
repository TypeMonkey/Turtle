package jg.cs.inter;

import java.util.Map;

import jg.cs.common.FunctionSignature;
import jg.cs.common.types.Type;
import jg.cs.inter.IRCompiler.LabelAndFunc;
import jg.cs.inter.instruction.Instr;

/**
 * Stores the results of compiling a Turtle program into Turtle IR
 * @author Jose
 *
 */
public class RunnableUnit {
  
  private final Map<FunctionSignature, LabelAndFunc> builtInLabels;
  private final Instr [] instructions;
  private final Type [] types;
  private final int mainLabel;
  
  /**
   * Constructs a Result
   * @param builtInLabel - the IR labels of the built-in functions of Turtle
   * @param instructions - the IR Turtle Instructions 
   * @param types - the type codes of compiled for this program
   * @param mainLabel - the index at which that main label is
   */
  public RunnableUnit(Map<FunctionSignature, 
      LabelAndFunc> builtInLabel, 
      Instr [] instructions, 
      Type [] types,
      int mainLabel) {
    this.builtInLabels = builtInLabel;
    this.instructions = instructions;
    this.types = types;
    this.mainLabel = mainLabel;
  }

  /**
   * Returns the IR labels for the Turtle built-in functions
   * @return the IR labels for the Turtle built-in functions
   */
  public Map<FunctionSignature, LabelAndFunc> getBuiltInLabels() {
    return builtInLabels;
  }

  /**
   * Returns the Turtle IR instructions
   * @return the Turtle IR instructions
   */
  public Instr[] getInstructions() {
    return instructions;
  }
  
  /**
   * Returns the type codes of this program
   * @return the type codes of this program
   */
  public Type[] getTypes() {
    return types;
  }
  
  /**
   * Returns the index of the main label
   * @return the index of the main label
   */
  public int getMainLabel() {
    return mainLabel;
  }
}
