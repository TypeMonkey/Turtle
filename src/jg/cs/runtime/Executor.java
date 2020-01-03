package jg.cs.runtime;

import java.util.HashMap;
import java.util.Map;

import jg.cs.inter.RunnableUnit;
import jg.cs.inter.instruction.DataLabelInstr;
import jg.cs.inter.instruction.IncfpInstr;
import jg.cs.inter.instruction.Instr;
import jg.cs.inter.instruction.JumpInstr;
import jg.cs.inter.instruction.JumpInstr.Jump;
import jg.cs.inter.instruction.LabelInstr;
import jg.cs.inter.instruction.LoadInstr;
import jg.cs.inter.instruction.LoadInstr.LoadType;
import jg.cs.inter.instruction.MutateInstr;
import jg.cs.inter.instruction.NoArgInstr;
import jg.cs.inter.instruction.NoArgInstr.NAInstr;
import jg.cs.inter.instruction.RetrieveInstr;
import jg.cs.inter.instruction.StoreInstr;
import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;
import jg.cs.runtime.alloc.OperandStack;

public class Executor {
  
  public static final long TRUE = 3;
  public static final long FALSE = 1;
  public static final long TAG_MASK = 0x0000000000000001L;

  private final FunctionStack fstack;
  private final OperandStack operandStack;
  private final HeapAllocator heapAllocator;
  private final RunnableUnit program;
  
  /**
   * Maps string labels to instruction line numbers
   */
  private Map<String, Integer> labelMap;
  
  private int instructionIndex;
  
  /**
   * Constructs an Executor
   * @param fstack
   * @param operandStack
   * @param allocator
   * @param program
   */  
  public Executor(FunctionStack fstack, 
      OperandStack operandStack, 
      HeapAllocator allocator,
      RunnableUnit program) {
    this.fstack = fstack;
    this.operandStack = operandStack;
    this.heapAllocator = allocator;
    this.program = program;
  }
  
  /**
   * Initializes this executor
   */
  public void init() {
    //map string labels to instr lines
    
    labelMap = new HashMap<>();
    
    /*
     * We are guarenteed that if a program has declared
     * n-structs, lines 4,5,6....n are the labels to those
     * structs constructors.
     * 
     * Primitive types (int, bool, string) are natively handled 
     * by the executor
     */
    for(int i = 3; i < program.getInstructions().length; i++) {
      //-3 since we don't want to count int, bool, string
      //as one of the custom struct defs
      if (program.getInstructions()[i] instanceof DataLabelInstr) {
        DataLabelInstr dataLabel = (DataLabelInstr) program.getInstructions()[i];
        labelMap.put(dataLabel.getTypeName(), i);
      }
      else if(program.getInstructions()[i] instanceof LabelInstr){
        LabelInstr label = (LabelInstr) program.getInstructions()[i];
        labelMap.put(label.getLabel(), i);
      }
    }
    
    instructionIndex = program.getMainLabel();
    
    System.out.println("----LABEL MAP: "+labelMap);
  }
  
  public void execute() {
    System.out.println("==========================EXECUTING=========================="); 
    
    /*
     * All values in Turtle are 64-bit
     * 
     * The right most bit is used as a tag bit. 1 -> Primitive, 0 -> Reference
     * 
     * When loading a constant, the constant must be encoded to fit with this rule
     * When loading from an address, there is no need to encode. But decoding may be useful
     * 
     * Store instructions expect the top most value to be encoded.
     * 
     * Booleans: True = 3, False = 1
     */
    for( ; instructionIndex < program.getInstructions().length ; instructionIndex++) {
      if (program.getInstructions()[instructionIndex] != null) {
        execInstr(program.getInstructions()[instructionIndex]);
        
        //System.out.println("!!!!!! STACK \n"+fstack);
        //System.out.println(operandStack);
      }
    }
  }
  
  private void execInstr(Instr instr) {
    //System.out.println("---CURRENT["+instructionIndex+"]: "+instr);
    if (instr instanceof DataLabelInstr) {
      execDataLabel((DataLabelInstr) instr);
    }
    else if (instr instanceof JumpInstr) {
      execJump((JumpInstr) instr);
    }
    else if (instr instanceof LabelInstr) {
      /*
       * Only do something if label is builtin
       */
      execBuiltInFunc((LabelInstr) instr);
    }
    else if (instr instanceof LoadInstr<?>) {
      execLoad((LoadInstr<?>) instr);
    }
    else if (instr instanceof MutateInstr) {
      execMute((MutateInstr) instr);
    }
    else if (instr instanceof NoArgInstr) {
      execNoArg((NoArgInstr) instr);
    }
    else if (instr instanceof RetrieveInstr) {
      execRet((RetrieveInstr) instr);
    }
    else if (instr instanceof StoreInstr) {
      execStore((StoreInstr) instr);
    }
    else if (instr instanceof IncfpInstr) {
      IncfpInstr fpChangeInstr = (IncfpInstr) instr;
      fstack.changeFPBy(fpChangeInstr.getAmount());
    }
  }
  
  private void execDataLabel(DataLabelInstr instr) {
    /*
     * Do heap allocation here
     * 
     * TOP most value on stack = bottom-most member declaration
     */   
    operandStack.pushOperand(heapAllocator.allocate(operandStack, instr.getTypeCode()));
  }
  
  private void execJump(JumpInstr instr) {
    //System.out.println(" CALL/JUMP INSTR: "+instr);
    if (instr.getJump() == Jump.JMP) {
      instructionIndex = translateToIndex(instr.getTargetLabel());
    }
    else if (instr.getJump() == Jump.JMPN) {
      instructionIndex = operandStack.popOperand() == FALSE ?
                           translateToIndex(instr.getTargetLabel()) : instructionIndex;
    }
    else if (instr.getJump() == Jump.JMPT) {
      instructionIndex = operandStack.popOperand() == TRUE ? 
          translateToIndex(instr.getTargetLabel()) : instructionIndex;
    }
    else if (instr.getJump() == Jump.CALL) {
      instructionIndex = translateToIndex(instr.getTargetLabel()) - 1;
    }
    //System.out.println("---NEW INDEX "+instructionIndex);
  }
  
  private void execLoad(LoadInstr<?> instr) {
    if (instr.getType() == LoadType.ICLOAD) {
      long constant = ((LoadInstr<Long>) instr).getValue();
      constant = constant << 1;
      constant += 1;

      operandStack.pushOperand(constant);
    }
    else if (instr.getType() == LoadType.SCLOAD) {
      long addr = heapAllocator.allocate(instr.getValue().toString());
      
      //System.out.println(" ---> LOADING CONS STRING: "+(addr >>> 1));
      
      //System.out.println(" ---just in case: \n"+heapAllocator.getHeapRepresentation());
      
      operandStack.pushOperand(addr);
    }
    else if (instr.getType() == LoadType.MLOAD) {
      long offset = ((LoadInstr<Long>) instr).getValue();
      long value = fstack.retrieveAtOffset(offset);
      
      //System.out.println("----> LOADING FROM STACK: "+(value >>> 1));
      
      //System.out.println(" ---just in case: \n"+heapAllocator.getHeapRepresentation());
      
      operandStack.pushOperand(value);
    }
  }
  
  private void execMute(MutateInstr instr) {
    long targetAddress = operandStack.popOperand();
    long newValue = operandStack.popOperand();
    
    heapAllocator.mutate(targetAddress, instr.getOffset(),newValue);
    
    operandStack.pushOperand(targetAddress);
  }
  
  private void execNoArg(NoArgInstr instr) {
    if (instr.getInstr() == NAInstr.EQUAL) {
      long left = operandStack.popOperand();
      long right = operandStack.popOperand();
      
      //System.out.println(" --- EQUAL??? l = "+(left >>> 1)+"  ,  r = "+(right >>> 1));
      
      operandStack.pushOperand(left == right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.IADD) {
      long left = operandStack.popOperand();
      long right = operandStack.popOperand() - 1; //decode right
      
      long result = left + right;
      
      operandStack.pushOperand(result);
    }
    else if (instr.getInstr() == NAInstr.SADD) {
      //System.out.println("---CURRENT["+instructionIndex+"]: "+instr);

      //System.out.println("---SADD OSTACK: \n"+operandStack);
      long right = operandStack.popOperand(); 
      long left = operandStack.popOperand();
      
      //System.out.println("---> SADD| LEFT ADDR: "+(left >>> 1)+"  ,  RIGHT ADDR:  "+(right >>> 1));
      
      String leftStr = heapAllocator.getString(left);
      String rightStr = heapAllocator.getString(right);
      
      //System.out.println(" SADD: Left: '"+leftStr+"'");
      //System.out.println(" SADD: Right: '"+rightStr+"'");

      
      //System.out.println("==================================SADD RESULT: "+leftStr+rightStr);

      long resultAddress = heapAllocator.allocate(leftStr + rightStr);
      operandStack.pushOperand(resultAddress);
      
      //System.out.println("====================================REREAD: "+heapAllocator.getString(resultAddress));
    }
    else if (instr.getInstr() == NAInstr.IGREAT) {
      long right = operandStack.popOperand();
      long left = operandStack.popOperand();
      
      operandStack.pushOperand(left > right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.IGREATQ) {
      long right = operandStack.popOperand();
      long left = operandStack.popOperand();
      
      operandStack.pushOperand(left >= right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.ILESS) {
      long right = operandStack.popOperand();
      long left = operandStack.popOperand();
      
      operandStack.pushOperand(left < right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.ILESSQ) {
      long right = operandStack.popOperand();
      long left = operandStack.popOperand();
      
      operandStack.pushOperand(left <= right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.NOTEQ) {
      long right = operandStack.popOperand();
      long left = operandStack.popOperand();
      
      operandStack.pushOperand(left != right ? TRUE : FALSE);
    }
    else if (instr.getInstr() == NAInstr.ISUB) {
      //System.out.println("---ISUB OSTACK: \n"+operandStack);

      long right = operandStack.popOperand() - 1; //decode right
      long left = operandStack.popOperand() - 1; //decode left
      
      long result = (left - right) + 1;
      
      //System.out.println("----ISUB: LEFT="+(left >>> 1)+" | RIGHT="+(right >>> 1)+" , res="+(result >>> 1));
      
      operandStack.pushOperand(result);
    }
    else if (instr.getInstr() == NAInstr.IMUL) {
      long left = operandStack.popOperand() >>> 1; //decode left
      long right = operandStack.popOperand() >>> 1; //decode right
      
      long result = left * right;
      if ((result & TAG_MASK) != 1) {
        result += 1;
      }
      
      operandStack.pushOperand(result);
    }
    else if (instr.getInstr() == NAInstr.RET) {
      instructionIndex = (int) fstack.retrieveAtOffset(0) - 1;
      //System.out.println(" INIT INSTR INDEX CHANGE: "+instructionIndex);
      /* The reason we do -1 is because the interpreter loop will
       * Skip over changes to instructionIndex as it is changed DURING the loop.
       * By doing -1, it'll increment it by 1 in the next loop and execute our desired instruction
       */
      
      long callResult = operandStack.popOperand();
      long callerFp = operandStack.popOperand();
      
      
      fstack.setFP(callerFp);     
      
      //System.out.println("---PUSHING CALL RESULT: "+callerFp);
      operandStack.pushOperand(callResult);
      
      //System.out.println("---RET: MAX: "+(program.getInstructions().length - 1) + " | change: "+instructionIndex);
    }
    else if (instr.getInstr() == NAInstr.PUSHFP) {      
      //System.out.println("  SAVING FP="+fstack.getCurrentFP()+" on OPSTACK");
      operandStack.pushOperand(fstack.getCurrentFP());
    }
    else if (instr.getInstr() == NAInstr.SAVEINS) {
      //System.out.println("---SAVING INSTRUCITON INDEX: cur="+instructionIndex+" , modi="+(instructionIndex + 2));
      fstack.saveAtOffset(0, instructionIndex+2);
    }
  }
  
  private void execRet(RetrieveInstr instr) {
    long address = operandStack.popOperand();
    
    address = address >> 1; //decode reference
    
    operandStack.pushOperand(heapAllocator.get(address, instr.getOffset()));
  }
  
  private void execStore(StoreInstr instr) {
    long address = instr.getIndex();
    long value = operandStack.popOperand();
    
    fstack.saveAtOffset(address, value);
  }
  
  private void execBuiltInFunc(LabelInstr labelInstr) {
    if (labelInstr.isBuiltin()) {
      //System.out.println("----CALLING BUILT IN "+operandStack);
      BuiltInExecutor.executeBuiltIn(labelInstr.getRiCode(), fstack, operandStack, heapAllocator);
    }
    //if not a built in label, don't do anything
  }
  
  private int translateToIndex(String label) {
    return labelMap.get(label);
  }
}
