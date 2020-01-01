package jg.cs.inter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jg.cs.common.BuiltInFunctions;
import jg.cs.common.FunctionLike;
import jg.cs.common.FunctionSignature;
import jg.cs.common.OperatorKind;
import jg.cs.common.types.Type;
import jg.cs.compile.Program;
import jg.cs.compile.errors.TypeMismatchException;
import jg.cs.compile.errors.UnresolvableComponentException;
import jg.cs.compile.nodes.BinaryOpExpr;
import jg.cs.compile.nodes.DataDeclaration;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.nodes.FunctDefExpr;
import jg.cs.compile.nodes.FunctionCall;
import jg.cs.compile.nodes.IdenTypeValTuple;
import jg.cs.compile.nodes.IfExpr;
import jg.cs.compile.nodes.LetExpr;
import jg.cs.compile.nodes.MutateExpr;
import jg.cs.compile.nodes.RetrieveExpr;
import jg.cs.compile.nodes.SetExpr;
import jg.cs.compile.nodes.WhileExpr;
import jg.cs.compile.nodes.atoms.BinaryOperator;
import jg.cs.compile.nodes.atoms.Bool;
import jg.cs.compile.nodes.atoms.Identifier;
import jg.cs.compile.nodes.atoms.Int;
import jg.cs.compile.nodes.atoms.NullValue;
import jg.cs.compile.nodes.atoms.Str;
import jg.cs.compile.nodes.atoms.Typ;
import jg.cs.inter.instruction.DataLabelInstr;
import jg.cs.inter.instruction.Instr;
import jg.cs.inter.instruction.JumpInstr;
import jg.cs.inter.instruction.JumpInstr.Jump;
import jg.cs.inter.instruction.LabelInstr;
import jg.cs.inter.instruction.LoadInstr;
import jg.cs.inter.instruction.LoadInstr.LoadType;
import jg.cs.inter.instruction.MutateInstr;
import jg.cs.inter.instruction.NoArgInstr.NAInstr;
import jg.cs.inter.instruction.RetrieveInstr;
import jg.cs.inter.instruction.StoreInstr;
import jg.cs.inter.instruction.NoArgInstr;


/**
 * Compiles a type-checked, structurally sound Turtle program into 
 * the Turtle IR Language. 
 * @author Jose
 */
public class IRCompiler {

  /**
   * A helpful class that bundles compiled instructions with type information
   * @author Jose
   *
   */
  public static class InstrAndType{
    
    private final List<Instr> instrs;
    private final Type type;
    
    public InstrAndType(List<Instr> instrs, Type type) {
      this.instrs = instrs;
      this.type = type;
    }
    
    public InstrAndType(Type type, Instr ... instrs) {
      this.instrs = new ArrayList<Instr>(Arrays.asList(instrs));
      this.type = type;
    }

    public List<Instr> getInstrs() {
      return instrs;
    }

    public Type getType() {
      return type;
    }  
  }
  
  /**
   * A helpful class that bundles a variable's type and index on the stack
   * @author Jose
   */
  public static class TypeAndIndex{
    
    private final long index;
    private final Type type;
    
    public TypeAndIndex(long index, Type type) {
      this.index = index;
      this.type = type;
    }

    public long getIndex() {
      return index;
    }

    public Type getType() {
      return type;
    }  
    
    @Override
    public String toString() {
      return " TYPE: "+type+" | "+index;
    }
  }
  
  /**
   * A helpful class that bundles a function and it's IR label
   * @author Jose
   *
   */
  public static class LabelAndFunc{
    private final String label;
    private final FunctionLike function;
    
    public LabelAndFunc(String label, FunctionLike function) {
      this.label = label;
      this.function = function;
    }

    public String getLabel() {
      return label;
    }

    public FunctionLike getFunction() {
      return function;
    }  
    
    @Override
    public String toString() {
      return " SIG: "+function.getIdentity()+" | "+label;
    }
  }
  
  private final Program program;
  
  public IRCompiler(Program program) {
    this.program = program;
  }
  
  public RunnableUnit compile(){
    ArrayList<Instr> instrs = new ArrayList<Instr>();
    
    /*
     * Type codes:
     * At the beginning of each IR instruction listing, 
     * struct declarations are laid out.
     * 
     *  say we have (data Data (a:int b:bool c:string d:Data))
     *  
     *  The IR listing would begin as:
     *  
     *  -int:
     *  -bool:
     *  -string:
     *  -Data:0,1,2,3
     *  
     *  int will always be typecode 0
     *  bool will always be typecode 1
     *  string will always be typecode 2
     *  
     *  struct declarations will then be declared after those primitive types
     *  
     *  Their typecode corresponds with their line number minus 1
     */
    
    LinkedHashMap<Type, Integer> typeCodes = new LinkedHashMap<>();
    //load primitive types into typecodes
    typeCodes.put(Type.INTEGER, 0);
    typeCodes.put(Type.BOOLEAN, 1);
    typeCodes.put(Type.STRING, 2);
    
    //place primitives in instructions
    instrs.add(new DataLabelInstr(Type.INTEGER.getName(), new int[0], -1, -1));
    instrs.add(new DataLabelInstr(Type.BOOLEAN.getName(), new int[0], -1, -1));
    instrs.add(new DataLabelInstr(Type.STRING.getName(), new int[0], -1, -1));

    
    /*
     * Assign labels to top level functions, and built in functions
     */
    HashMap<FunctionSignature, LabelAndFunc> fmap = new HashMap<>();
    
    HashMap<FunctionSignature, LabelAndFunc> builtInLabel = new HashMap<>();
    
    //add built-ins
    for (Entry<FunctionSignature, BuiltInFunctions> builtin : BuiltInFunctions.BUILT_IN_MAP.entrySet()) {
      LabelAndFunc bilt = new LabelAndFunc(genLabel(builtin.getKey().getName()), builtin.getValue());
      fmap.put(builtin.getValue().getIdentity().getSignature(), 
          bilt);
      
      builtInLabel.put(builtin.getValue().getIdentity().getSignature(), bilt);
    }  
    
    int dataCode = 3;
    for (Entry<FunctionSignature, FunctionLike> topFunction : program.getFileFunctions().entrySet()) {
      if (topFunction.getValue() instanceof DataDeclaration) {
        typeCodes.put(topFunction.getValue().getIdentity().getReturnType(), dataCode);
        dataCode++;
      }
      fmap.put(topFunction.getKey(), 
          new LabelAndFunc(genLabel(topFunction.getKey().getName()), topFunction.getValue()));
    }
    
    System.out.println("__TYPE CODES: "+typeCodes);
    
    //go over struct declarations and put their type codes
    for (Entry<Type, DataDeclaration> dec : program.getStructDecs().entrySet()) {
      int [] tcodes = new int[dec.getValue().getMembers().size()];
      int i = 0;
      for(Entry<Identifier, Typ> mem : dec.getValue().getMembers().entrySet()) {
        tcodes[i] = typeCodes.get(mem.getValue().getActualValue());
        i++;
      }
      
      instrs.add(
          new DataLabelInstr(fmap.get(dec.getValue().getIdentity().getSignature()).label,
              tcodes, dec.getValue().getLeadLnNumber(), dec.getValue().getLeadColNumber()));
    }
        
    //Compile top-level functions
    for (FunctionLike funcs : program.getFileFunctions().values()) {
      if (funcs instanceof FunctDefExpr) {
        InstrAndType s = compileFuncDef((FunctDefExpr) funcs, new ArrayList<>(), fwrap(fmap), 0);
        
        /*
         * TODO: Remove these after debugging. These are just helpful for separating function code visually
         */
        instrs.add(null);
        
        instrs.addAll(s.instrs);
        
        /*
         * TODO: Remove these after debugging. These are just helpful for separating function code visually
         */
        instrs.add(null);
      }
    }
    
    //entry point label that'll act as out main
    int mainIndex = instrs.size();
    instrs.add(new LabelInstr(genLabel("entryPoint"), -1, -1));
    
    //parse top-level statements
    for (Expr x : program.getExprList()) {
      /*
       * TODO: Remove these after debugging. These are just helpful for separating code visually
       */
      instrs.add(null);
     
      instrs.addAll(compileExpr(x, new ArrayList<>(), fwrap(fmap), 0).instrs);
      
      /*
       * TODO: Remove these after debugging. These are just helpful for separating code visually
       */
      instrs.add(null);
    }
    
    //make typecode array
    Type [] types = new Type[3 + program.getStructDecs().size()];
    for (Entry<Type, Integer> t : typeCodes.entrySet()) {
      types[t.getValue()] = t.getKey();
    }
    
    return new RunnableUnit(fmap, instrs.toArray(new Instr[instrs.size()]), types, mainIndex);
  }
  
  private InstrAndType compileExpr(Expr expr,  
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex){
    if (expr instanceof Int) {
      return compileInt((Int )expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof Bool) {
      return compileBool((Bool) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof Str) {
      return compileStr((Str) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof NullValue) {
      NullValue nullValue = (NullValue) expr;
      return new InstrAndType(nullValue.getActualValue().getActualValue(), 
          new LoadInstr<Long>(LoadType.MLOAD, 0L, nullValue.getLeadLnNumber(), nullValue.getLeadColNumber()));
    }
    else if (expr instanceof Identifier) {
      return compileIden((Identifier) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof FunctDefExpr) {
      return compileFuncDef((FunctDefExpr) expr, indexMaps, fMaps, 0);
    }
    else if (expr instanceof LetExpr) {
      return compileLet((LetExpr) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof IfExpr) {
      return compileIf((IfExpr) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof BinaryOpExpr) {
      return compileBinOp((BinaryOpExpr) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof SetExpr) {
      return compileSet((SetExpr) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof FunctionCall) {
      return compileFuncCall((FunctionCall) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof WhileExpr) {
      return compileWhile((WhileExpr) expr, indexMaps, fMaps, stackIndex);
    }
    else if (expr instanceof MutateExpr) {
      MutateExpr mutateExpr = (MutateExpr) expr;
      
      ArrayList<Instr> instrs = new ArrayList<Instr>();
      
      InstrAndType target = compileExpr(mutateExpr.getTarget(), indexMaps, fMaps, stackIndex);
      InstrAndType newValResult = compileExpr(mutateExpr.getNewValue(), indexMaps, fMaps, stackIndex);
      
      TypeAndIndex offset = getOffset(mutateExpr.getMemberName().getActualValue(), target.type);
      
      instrs.addAll(newValResult.instrs);
      instrs.addAll(target.instrs);
      instrs.add(new MutateInstr(offset.index, mutateExpr.getLeadLnNumber(), mutateExpr.getLeadColNumber()));
      
      return new InstrAndType(instrs, target.getType());
    }
    else if (expr instanceof RetrieveExpr) {
      RetrieveExpr retrieveExpr = (RetrieveExpr) expr;
      ArrayList<Instr> instrs = new ArrayList<Instr>();

      InstrAndType target = compileExpr(retrieveExpr.getTarget(), indexMaps, fMaps, stackIndex);
      instrs.addAll(target.instrs);
      
      TypeAndIndex offset = getOffset(retrieveExpr.getMemberName().getActualValue(), target.type);
      
      instrs.add(new RetrieveInstr(offset.index, retrieveExpr.getLeadLnNumber(), retrieveExpr.getLeadColNumber()));
      
      return new InstrAndType(instrs, offset.type);
    }
    else if (expr instanceof DataDeclaration) {
      
    }
    
    //System.out.println("unknown? "+expr.getClass());
    return null;
  }

  private InstrAndType compileInt(Int expr,  
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps, 
      long stackIndex){
    
    long value = expr.getActualValue();
    return new InstrAndType(Type.INTEGER, new LoadInstr<Long>(LoadType.ICLOAD, value, expr.getLeadLnNumber(), expr.getLeadColNumber()));
  }
  
  private InstrAndType compileBool(Bool expr,  
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex){
    
    long value = expr.getActualValue() ? 1 : 0;
    return new InstrAndType(Type.BOOLEAN, new LoadInstr<Long>(LoadType.ICLOAD, value, expr.getLeadLnNumber(), expr.getLeadColNumber()));
  }
  
  private InstrAndType compileStr(Str expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    return new InstrAndType(Type.STRING, new LoadInstr<String>(LoadType.SCLOAD, expr.clipQuotes(), expr.getLeadLnNumber(), expr.getLeadColNumber()));
  }
  
  private InstrAndType compileIden(Identifier expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    TypeAndIndex varTypeAndIndex = find(expr.getActualValue(), indexMaps);
        
    return new InstrAndType(Type.BOOLEAN, 
        new LoadInstr<Long>(LoadType.MLOAD, varTypeAndIndex.index, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    /*
    //check for the three primitives
    if (varTypeAndIndex.getType().equals(Type.BOOLEAN)) {
      return new InstrAndType(Type.BOOLEAN, 
          new LoadInstr<Long>(LoadType.MLOAD, varTypeAndIndex.index, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    }
    else if (varTypeAndIndex.getType().equals(Type.INTEGER)) {
      return new InstrAndType(Type.INTEGER, 
          new LoadInstr<Long>(LoadType.ILOAD, varTypeAndIndex.index, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    }
    else if (varTypeAndIndex.getType().equals(Type.STRING)) {
      return new InstrAndType(Type.STRING, 
          new LoadInstr<Long>(LoadType.SLOAD, varTypeAndIndex.index, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    }
    else {
      return new InstrAndType(varTypeAndIndex.type, 
          new LoadInstr<Long>(LoadType.RLOAD, varTypeAndIndex.index, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    }
    */
  }
  
  private InstrAndType compileFuncDef(FunctDefExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();
    
    //add function label
    instrs.add(new LabelInstr(genLabel(expr.getFuncName().getImage()), expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    //local variable map
    HashMap<String, TypeAndIndex> localEnv = new HashMap<String, TypeAndIndex>();
    
    //assign indices to function arguments first
    for (Entry<String, IdenTypeValTuple> param : expr.getParameters().entrySet()) {
      localEnv.put(param.getKey(), new TypeAndIndex(stackIndex, param.getValue().getType()));
      stackIndex++;
    }
    
    //map to keep track of nested functions
    HashMap<FunctionSignature, LabelAndFunc> nested = new HashMap<>();
    LabelAndFunc thisFunc = new LabelAndFunc(genLabel(expr.getFuncName().getImage()), expr);
    nested.put(expr.getIdentity().getSignature(), thisFunc);
    
    //then, compile the function's body
    for (Expr bod : expr.getExpressionsExprs()) {
      if (bod instanceof FunctDefExpr) {
        FunctDefExpr nestedFunc = (FunctDefExpr) bod;
        
        //need instructions to pass over this function
        String skipLabel = genLabel("skipNested");     
        instrs.add(new JumpInstr(Jump.JMP, skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
        
        InstrAndType result = compileFuncDef(nestedFunc, concatToFront(localEnv, indexMaps), fconcat(nested, fMaps), 0);
        instrs.addAll(result.instrs);
        
        LabelAndFunc labelAndFunc = new LabelAndFunc(genLabel(nestedFunc.getFuncName().getImage()), nestedFunc);
        nested.put(nestedFunc.getIdentity().getSignature(), labelAndFunc);
        
        //label to skip to so that nested function isn't executed
        instrs.add(new LabelInstr(skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
      }
      else {
        InstrAndType result = compileExpr(bod, concatToFront(localEnv, indexMaps), fconcat(nested, fMaps), stackIndex);
        instrs.addAll(result.instrs);
      }
    }
    
    //add ret instruction to conclude function
    instrs.add(new NoArgInstr(NAInstr.RET, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    return new InstrAndType(instrs, expr.getType());
  }
  
  private InstrAndType compileLet(LetExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();
    
    //local variable map
    HashMap<String, TypeAndIndex> localEnv = new HashMap<String, TypeAndIndex>();
    
    //set indexes of local variables and their values
    for (Entry<String, IdenTypeValTuple> localVar : expr.getVars().entrySet()) {     
      Expr valueExpr = localVar.getValue().getValue();
      
      InstrAndType result = compileExpr(valueExpr, concatToFront(localEnv, indexMaps), fMaps, stackIndex);
      instrs.addAll(result.getInstrs());
      
      instrs.add(new StoreInstr(stackIndex, valueExpr.getLeadLnNumber(), valueExpr.getLeadColNumber()));      
      localEnv.put(localVar.getKey(), new TypeAndIndex(stackIndex, result.getType()));
      stackIndex++;
    }
    
    //now, compile the let body
    InstrAndType resultType = null;
    HashMap<FunctionSignature, LabelAndFunc> nested = new HashMap<>();
    
    for (Expr bod : expr.getExpressions()) {
      if (bod instanceof FunctDefExpr) {
        FunctDefExpr nestedFunc = (FunctDefExpr) bod;
        
        //need instructions to pass over this function
        String skipLabel = genLabel("skipNested");     
        instrs.add(new JumpInstr(Jump.JMP, skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
        
        resultType = compileFuncDef(nestedFunc, concatToFront(localEnv, indexMaps), fconcat(nested, fMaps), 0);
        instrs.addAll(resultType.instrs);
        
        LabelAndFunc labelAndFunc = new LabelAndFunc(genLabel(nestedFunc.getFuncName().getImage()), nestedFunc);
        nested.put(nestedFunc.getIdentity().getSignature(), labelAndFunc);
        
        //label to skip to so that nested function isn't executed
        instrs.add(new LabelInstr(skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
      }
      else {
        resultType = compileExpr(bod, concatToFront(localEnv, indexMaps), fconcat(nested, fMaps), stackIndex);
        instrs.addAll(resultType.instrs);
      }
    }
    
    return new InstrAndType(instrs, resultType.getType());
  }
  
  private InstrAndType compileIf(IfExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();

    //compile condition first
    InstrAndType condResult = compileExpr(expr.getCondition(), indexMaps, fMaps, stackIndex);
    instrs.addAll(condResult.instrs);
    
    //make a few labels
    String trueLabel = genLabel("trueLabel");
    String endLabel = genLabel("endIfLabel");

    
    //check topmost stack
    instrs.add(new JumpInstr(Jump.JMPT, trueLabel, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    //append false branch right after cond jump
    InstrAndType falseBranch = compileExpr(expr.getFalseConseq(), indexMaps, fMaps, stackIndex);
    instrs.addAll(falseBranch.instrs);
    instrs.add(new JumpInstr(Jump.JMP, endLabel, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    InstrAndType trueBranch = compileExpr(expr.getTrueConseq(), indexMaps, fMaps, stackIndex);
    instrs.add(new LabelInstr(trueLabel, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    instrs.addAll(trueBranch.instrs);
    
    instrs.add(new LabelInstr(endLabel, expr.getLeadLnNumber(), expr.getLeadColNumber()));
    
    return new InstrAndType(instrs, falseBranch.getType());
  }
 
  private InstrAndType compileBinOp(BinaryOpExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();

    BinaryOperator rawBinOp = expr.getOperator();
    OperatorKind operator = rawBinOp.getActualValue();

    InstrAndType leftInstr = compileExpr(expr.getLeft(), indexMaps, fMaps, stackIndex);
    InstrAndType rightInstr = compileExpr(expr.getRight(), indexMaps, fMaps, stackIndex);

    instrs.addAll(leftInstr.instrs);
    instrs.addAll(rightInstr.instrs);

    Type result = null;
    if (operator == OperatorKind.PLUS) {
      if (leftInstr.getType().equals(Type.STRING) || rightInstr.getType().equals(Type.STRING)) {
        result = Type.STRING;
        instrs.add(new NoArgInstr(NAInstr.SADD, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
      }
      else {
        result = Type.INTEGER;
        instrs.add(new NoArgInstr(NAInstr.IADD, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
      }
    }
    else if (operator == OperatorKind.MINUS) {
      result = Type.INTEGER;
      instrs.add(new NoArgInstr(NAInstr.ISUB, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.TIMES) {
      result = Type.INTEGER;
      instrs.add(new NoArgInstr(NAInstr.ISUB, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.LESS) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.ILESS, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.GREAT) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.IGREAT, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.EQUAL) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.EQUAL, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.NOTEQUAL) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.NOTEQ, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.GREATQ) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.IGREATQ, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.LESSQ) {
      result = Type.BOOLEAN;
      instrs.add(new NoArgInstr(NAInstr.ILESSQ, rawBinOp.getLeadLnNumber(), rawBinOp.getLeadColNumber()));
    }
    else if (operator == OperatorKind.EXP) {
      /**
       * NOT SUPPORTED RIGHT NOW
       */
    }

    return new InstrAndType(instrs, result);
  }
  
  private InstrAndType compileSet(SetExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();
    
    InstrAndType newValueResult = compileExpr(expr.getValue(), indexMaps, fMaps, stackIndex);
    instrs.addAll(newValueResult.instrs);
    
    TypeAndIndex var = find(expr.getIdentifier().getActualValue(), indexMaps);
    
    instrs.add(new StoreInstr(var.index, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
    
    return new InstrAndType(instrs, newValueResult.type);
  }

  private InstrAndType compileFuncCall(FunctionCall expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    // TODO Auto-generated method stub
    ArrayList<Instr> instrs = new ArrayList<Instr>();
    
    int startIndex = 0;
    
    Type [] argTypes = new Type[expr.getArguments().size()];
    for (Expr arg : expr.getArguments()) {
      InstrAndType argResult = compileExpr(arg, indexMaps, fMaps, stackIndex);
      instrs.addAll(argResult.instrs);
      instrs.add(new StoreInstr(stackIndex, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
      argTypes[startIndex] = argResult.type;
      System.out.println("RESL "+argResult.type+" | "+arg+" | "+indexMaps);
      startIndex++;
      stackIndex++;
    }
    
    FunctionSignature targetFunction = new FunctionSignature(expr.getFuncName().getImage(), argTypes);
    
    LabelAndFunc found = find(targetFunction, fMaps);
    
    System.out.println(targetFunction + " ln: "+expr.getLeadLnNumber());
    
    if (BuiltInFunctions.BUILT_IN_MAP.containsKey(targetFunction)) {
      instrs.add(new JumpInstr(Jump.CALLI, found.label, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
    }
    else {
      instrs.add(new JumpInstr(Jump.CALL, found.label, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
    }
    
    return new InstrAndType(instrs, found.getFunction().getIdentity().getReturnType());
  }
  
  private InstrAndType compileWhile(WhileExpr expr, 
      List<Map<String, TypeAndIndex>> indexMaps, 
      List<Map<FunctionSignature, LabelAndFunc>> fMaps,
      long stackIndex) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();

    //compile condition
    InstrAndType conditionComp = compileExpr(expr.getCondition(), indexMaps, fMaps, stackIndex);
    instrs.addAll(conditionComp.instrs);
    
    //check condition
    String endLabel = genLabel("endWhile");
    instrs.add(new JumpInstr(Jump.JMPN, endLabel, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
    
    //map to keep track of nested functions
    HashMap<FunctionSignature, LabelAndFunc> nested = new HashMap<>();
    
    //then, compile the function's body
    InstrAndType result = null;
    for (Expr bod : expr.getExpressions()) {
      if (bod instanceof FunctDefExpr) {
        FunctDefExpr nestedFunc = (FunctDefExpr) bod;
        
        //need instructions to pass over this function
        String skipLabel = genLabel("skipNested");     
        instrs.add(new JumpInstr(Jump.JMP, skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
        
        result = compileFuncDef(nestedFunc, indexMaps, fconcat(nested, fMaps), 0);
        instrs.addAll(result.instrs);
        
        LabelAndFunc labelAndFunc = new LabelAndFunc(genLabel(nestedFunc.getFuncName().getImage()), nestedFunc);
        nested.put(nestedFunc.getIdentity().getSignature(), labelAndFunc);
        
        //label to skip to so that nested function isn't executed
        instrs.add(new LabelInstr(skipLabel, nestedFunc.getLeadLnNumber(), nestedFunc.getLeadColNumber()));
      }
      else {
        result = compileExpr(bod, indexMaps, fconcat(nested, fMaps), stackIndex);
        instrs.addAll(result.instrs);
      }
    }
    
    instrs.add(new LabelInstr(endLabel, expr.getLeadLnNumber(), expr.getLeadLnNumber()));
    
    return new InstrAndType(instrs, result.type);
  }
  
  //struct member to offset
  private TypeAndIndex getOffset(String member, Type targetType) {
    DataDeclaration dataDeclaration = program.getStructDecs().get(targetType);
    
    int i = 0;
    for (Entry<Identifier, Typ> p : dataDeclaration.getMembers().entrySet()) {
      if (p.getKey().getActualValue().equals(member)) {
        return new TypeAndIndex(i + 2, p.getValue().getActualValue());
      }
      i++;
    }
    
    return null;
  }
  
  ///helpful map functions

  private List<Map<String, TypeAndIndex>> concatToFront(
      Map<String, TypeAndIndex> newMap, 
      List<Map<String, TypeAndIndex>> others){
    ArrayList<Map<String, TypeAndIndex>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    newEnv.addAll(others);
    
    return newEnv;
  }
  
  private List<Map<FunctionSignature, LabelAndFunc>> fconcat(
      Map<FunctionSignature, LabelAndFunc> newMap, 
      List<Map<FunctionSignature, LabelAndFunc>> others){
    ArrayList<Map<FunctionSignature, LabelAndFunc>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    newEnv.addAll(others);
    
    return newEnv;
  }
  
  private List<Map<String, TypeAndIndex>> wrap(Map<String, TypeAndIndex> newMap){
    ArrayList<Map<String, TypeAndIndex>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    
    return newEnv;
  }
  
  private List<Map<FunctionSignature, LabelAndFunc>> fwrap(Map<FunctionSignature, LabelAndFunc> newMap){
    ArrayList<Map<FunctionSignature, LabelAndFunc>> newEnv = new ArrayList<>();
    newEnv.add(newMap);
    
    return newEnv;
  }
   
  private TypeAndIndex find(String identifier, List<Map<String, TypeAndIndex>> others){
    for (Map<String, TypeAndIndex> map : others) {
      TypeAndIndex result = map.get(identifier);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
  
  private LabelAndFunc find(FunctionSignature signature, List<Map<FunctionSignature, LabelAndFunc>> others){
    for (Map<FunctionSignature, LabelAndFunc> map : others) {
      LabelAndFunc result = map.get(signature);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
  
  // userfule utility method
  
  private static int labelNum = 0;
  
  public static String genLabel(String label) {   
    return label+(labelNum++);
  }
  
}
