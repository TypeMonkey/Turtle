package jg.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import jg.cs.CommandLineOption.CLOption;
import jg.cs.compile.Program;
import jg.cs.compile.StructureVerifier;
import jg.cs.compile.TypeChecker;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.parser.ExprBuilder;
import jg.cs.compile.parser.TurtleParser;
import jg.cs.compile.parser.TurtleTokenizer;
import jg.cs.inter.IRCompiler;
import jg.cs.inter.RunnableUnit;
import jg.cs.inter.instruction.Instr;
import jg.cs.runtime.Executor;
import jg.cs.runtime.alloc.FunctionStack;
import jg.cs.runtime.alloc.HeapAllocator;
import jg.cs.runtime.alloc.OperandStack;
import jg.cs.runtime.alloc.disc.DiskFunctionStack;
import jg.cs.runtime.alloc.disc.DiskHeapAllocator;
import jg.cs.runtime.alloc.disc.DiskOperandStack;
import jg.cs.runtime.alloc.mem.MemFunctionStack;
import jg.cs.runtime.alloc.mem.MemHeapAllocator;
import jg.cs.runtime.alloc.mem.MemOperandStack;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;
import net.percederberg.grammatica.parser.Token;
import net.percederberg.grammatica.parser.Tokenizer;

public class Main {

  private static final long DEFAULT_HEAP_SIZE = 1600;
  
  /**
   * Main driver method for the sNEK interpreter
   * @param args - the string arguments to the interpreter
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    CommandLineOption clOptions = parseArguments(args);
    if (clOptions == null) {
      System.exit(-1);
    }

    File targetFile = new File(clOptions.getSourceFile());
    if (targetFile.exists() && targetFile.canRead()) {

      List<Token> tokens = null;
      try {
        tokens = tokenizeSource(targetFile);
      } catch (FileNotFoundException e) {
        System.err.println("turtle: Provided source file is either nonexistant or unreadble!");
        System.exit(-1);
      } catch (ParseException e) {
        System.err.println("turtle: "+e.getMessage());
        System.exit(-1);
      }

      if (tokens == null) {
        System.err.println("Fatal Error: Couldn't create tokenizer! Exiting....");
        System.exit(-1);
      }

      List<Expr> components = null;
      try {
        components = parse(tokens);
      } catch (ParserLogException e) {
        System.err.println("turtle: "+e.getMessage());
        System.exit(-1);
      }

      if (components == null) {
        System.err.println("Fatal Error: Couldn't create parser! Exiting....");
        System.exit(-1);
      }

      System.out.println("-------------CHECKING STRUCTURE-------------");
      StructureVerifier verifier = new StructureVerifier(targetFile.getName(), components);
      Program program = verifier.verify();
      //System.out.println(components);

      System.out.println("-------------TYPE CHECKING-------------");
      TypeChecker typeChecker = new TypeChecker(program);
      typeChecker.checkType();

      System.out.println("-------------COMPILING-------------");
      IRCompiler compiler = new IRCompiler(program);
      RunnableUnit result = compiler.compile();

      
      if (clOptions.getValue(CLOption.IR_OUTPUT) != null) {
        //WRITE IR TO FILE FOR DEBUG
        System.out.println("---WRITING IR OUTPUT: "+clOptions.getValue(CLOption.IR_OUTPUT));
        PrintWriter writer = new PrintWriter(new FileOutputStream(clOptions.getValue(CLOption.IR_OUTPUT), false));
        Instr [] instrs = result.getInstructions();
        
        for (int i = 0; i < instrs.length; i++) {
          if (instrs[i] == null) {
            writer.println();
          }
          else {
            writer.println(instrs[i].toString());
          }
        }
        
        writer.close();
      }
      
      OperandStack operandStack = null;
      if (clOptions.getValue(CLOption.RAM_OP_STACK) != null) {
        operandStack = new MemOperandStack();
      }
      else {
        File operandFile = new File("opstack.data");
        operandStack = new DiskOperandStack(operandFile);
        if (clOptions.getValue(CLOption.NO_OP_STACK) != null) {
          operandFile.deleteOnExit();
        }
      }
                                   
      HeapAllocator heapAllocator = null;
      if (clOptions.getValue(CLOption.RAM_HEAP) != null) {
        heapAllocator = new MemHeapAllocator((int) clOptions.getMaxHeapSize());
      }
      else {
        File heapFile = new File("heap.data");
        heapAllocator = new DiskHeapAllocator(heapFile, clOptions.getMaxHeapSize());
        if (clOptions.getValue(CLOption.NO_HEAP) != null) {
          heapFile.deleteOnExit();
        }
      }
                                       
      FunctionStack functionStack = null;
      if (clOptions.getValue(CLOption.RAM_STACK) != null) {
        functionStack = new MemFunctionStack();
      }
      else {
        File ffile = new File("fstack.data");
        functionStack = new DiskFunctionStack(ffile);
        if (clOptions.getValue(CLOption.NO_F_STACK) != null) {
          ffile.deleteOnExit();
        }
      }

      //call GC 
      System.gc();
      
      Executor executor = new Executor(functionStack, operandStack, heapAllocator, result);
      executor.init();

      try {
        long elasped = executor.execute();
        if (clOptions.isPrintMeasure()) {
          System.out.println(">>>>>>> ELASPED "+elasped+" ns.....");
          System.out.println(">>>>>>>         "+(elasped/1000000)+" ms.....");
          System.out.println(">>>>>>>         "+(elasped/1000000000)+" secs.....");
        }
      } catch (Throwable e) {
        e.printStackTrace();
        System.err.println(functionStack);
        System.err.println(heapAllocator.getHeapRepresentation());      
      }       

    }
    else {
      System.err.println("turtle: Provided source file is either nonexistant or unreadble!");
    }

  }
  
  /**
   * Parses command line arguments
   * @param args - the String arguments provided
   * @return CommandLineOption representing entered arguments
   */
  public static CommandLineOption parseArguments(String [] args) {    
    Options options = new Options();
    
    Option help = new Option("h", "Lists command options for the Turtle interpreter");
    help.setLongOpt("help");
    help.setArgs(0);
    help.setRequired(false);
    
    Option heapSize = new Option("m", "Sets the max size of the heap, in bytes");
    heapSize.setLongOpt("max");
    heapSize.setArgs(1);
    heapSize.setRequired(false);
    
    Option irOutput = new Option("i", "Outputs the IR assembly code to a file. Helpful for debugging.");
    irOutput.setLongOpt("irout");
    irOutput.setArgs(1);
    irOutput.setRequired(false);
    
    Option elasped = new Option("l", "Prints out the execution time (in nanoseconds) of a Turtle program.");
    elasped.setLongOpt("msr");
    elasped.setArgs(0);
    elasped.setRequired(false);
    
    Option preserveOperands = new Option("nr", "Will delete operand stack data at the termination of the program.");
    preserveOperands.setLongOpt("nopstack");
    preserveOperands.setArgs(0);
    preserveOperands.setRequired(false);
    
    Option preserveStack = new Option("ns", "Will delete function stack data at the termination of the program.");
    preserveStack.setLongOpt("nofstack");
    preserveStack.setArgs(0);
    preserveStack.setRequired(false);
    
    Option preserveHeap = new Option("nh", "Will delete heap data at the termination of the program.");
    preserveHeap.setLongOpt("noheap");
    preserveHeap.setArgs(0);
    preserveHeap.setRequired(false);
    
    Option ramHeap = new Option("e", "Will load heap onto Random Access Memory.");
    ramHeap.setLongOpt("hload");
    ramHeap.setArgs(0);
    ramHeap.setRequired(false);
    
    Option ramFStack = new Option("s", "Will load the function stack onto Random Access Memory.");
    ramFStack.setLongOpt("sload");
    ramFStack.setArgs(0);
    ramFStack.setRequired(false);
    
    Option ramOStack = new Option("o", "Will load the operand stack onto Random Access Memory.");
    ramOStack.setLongOpt("oload");
    ramOStack.setArgs(0);
    ramOStack.setRequired(false);
    
    options.addOption(help);
    options.addOption(preserveStack);
    options.addOption(preserveHeap);
    options.addOption(preserveOperands);
    options.addOption(ramHeap);
    options.addOption(ramFStack);
    options.addOption(ramOStack);
    options.addOption(heapSize);
    options.addOption(irOutput);
    options.addOption(elasped);
    
    HelpFormatter usageFormatter = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();
    
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption("h")) {
        usageFormatter.printHelp("turtle [args] source_code.t", options);
        //System.out.println("---RETURNING NULL HELP");
        return null;
      }
      
      HashMap<CLOption, String> optionMap = new HashMap<>();
      long maxHeap = DEFAULT_HEAP_SIZE;
      boolean printElasped = false;
      
      if (commandLine.hasOption(elasped.getOpt()) || commandLine.hasOption(elasped.getLongOpt())) {
        printElasped = true;
      }
      
      if (commandLine.hasOption(preserveOperands.getOpt()) || commandLine.hasOption(preserveOperands.getLongOpt())) {
        optionMap.put(CLOption.NO_OP_STACK, "");
        //System.out.println("---DISK FUNC STACK "+optionMap);
      }
      if (commandLine.hasOption(preserveStack.getOpt()) || commandLine.hasOption(preserveStack.getLongOpt())) {
        optionMap.put(CLOption.NO_F_STACK, "");
        //System.out.println("---DISK HEAP");
      }
      if (commandLine.hasOption(preserveOperands.getOpt()) || commandLine.hasOption(preserveOperands.getLongOpt())) {
        optionMap.put(CLOption.NO_OP_STACK, "");
        //System.out.println("---DISK OP STACK");
      }
      
      if (commandLine.hasOption(ramOStack.getOpt()) || commandLine.hasOption(ramOStack.getLongOpt())) {
        optionMap.put(CLOption.RAM_OP_STACK, "");
        //System.out.println("---DISK FUNC STACK "+optionMap);
      }
      if (commandLine.hasOption(ramFStack.getOpt()) || commandLine.hasOption(ramFStack.getLongOpt())) {
        optionMap.put(CLOption.RAM_STACK, "");
        //System.out.println("---DISK HEAP");
      }
      if (commandLine.hasOption(ramHeap.getOpt()) || commandLine.hasOption(ramHeap.getLongOpt())) {
        optionMap.put(CLOption.RAM_HEAP, "");
        //System.out.println("---DISK OP STACK");
      }
      
      if (commandLine.hasOption("i") || commandLine.hasOption("irout")) {
        optionMap.put(CLOption.IR_OUTPUT, commandLine.hasOption("i") ? 
            commandLine.getOptionValue("i") : 
            commandLine.getOptionValue("irout"));
        //System.out.println("---DISK IR OUTPUT");
      }
      
      if (commandLine.hasOption("m") || commandLine.hasOption("max")) {
        String rawMax = commandLine.hasOption("m") ? 
                          commandLine.getOptionValue("m") : 
                          ( commandLine.hasOption("max") ?
                           commandLine.getOptionValue("max") : null );
        
        try {
          maxHeap = Long.parseLong(rawMax);
        } catch (NumberFormatException e) {
          usageFormatter.printHelp("turtle [args] source_code.t", options);
          return null;
        }       
      }
      
      if (commandLine.getArgList().size() != 1) {
        usageFormatter.printHelp("turtle [args] source_code.t", options);
        return null;
      }
      else{
        String sourceFile = commandLine.getArgList().get(0);    
        
        if (!getFileExtension(sourceFile).equals("t")) {
          usageFormatter.printHelp("turtle [args] source_code.t", options);
          return null;
        }
        
        return new CommandLineOption(maxHeap, optionMap, sourceFile, printElasped);
      }
    } catch (org.apache.commons.cli.ParseException e) {
      usageFormatter.printHelp("turtle [args] source_code.t", options);
      return null;
    }   
  }
  
  /**
   * Returns the extension of a file, without the "."
   * @param fileName - the name of the file
   * @return the file extension, or the empty string if no extension can't be found
   */
  public static String getFileExtension(String fileName) {
    int dotLI = fileName.lastIndexOf(".");
    if (dotLI < 0) {
      return "";
    }
    return fileName.substring(dotLI+1, fileName.length());
  }
  
  /**
   * Tokenizes a Turtle program
   * @param source - the entire source program as a string
   * @return a list of Tokens
   * @throws ParseException - if an unrecognizable token was detected
   * @throws FileNotFoundException 
   */
  public static List<Token> tokenizeSource(File source) throws ParseException, FileNotFoundException{
    try {
      ArrayList<String> nonCommentLines = new ArrayList<String>();
      BufferedReader reader = new BufferedReader(new FileReader(source));
      
      String temp = null;
      try {
        while ((temp = reader.readLine()) != null) {
          if (!temp.trim().startsWith("//")) {
            //then this line is not a comment
            nonCommentLines.add(temp);
          }
        }
      } catch (IOException e) {
        System.err.println("turtle: IO Error encountered while reading "+source.getName());
      }
      
      String allValidLines = nonCommentLines.stream().collect(Collectors.joining("\n"));
      
      //System.out.println("----------VALIDS:");
      //System.out.println(allValidLines);
      
      Tokenizer tokenizer = new TurtleTokenizer(new StringReader(allValidLines));
      ArrayList<Token> allTokens = new ArrayList<>();

      Token current = null;
      while((current = tokenizer.next()) != null) {
        allTokens.add(current);
      }

      return allTokens;
    } catch (ParserCreationException e) {
      return null;
    }
  }
  
  /**
   * Parses a list of tokens into a coherent abstract sturcture in the form 
   * of a list of expressions
   * @param tokens - list of Tokens to gather tokens from
   * @return a list of Expr
   * @throws ParserLogException - if invalid syntax was detected
   */
  public static List<Expr> parse(List<Token> tokens) throws ParserLogException{
    try {
      ExprBuilder builder = new ExprBuilder();
      TurtleParser parser = new TurtleParser(null, builder);

      parser.parseFromTokenList(tokens);
      
      return new ArrayList<>(builder.getStackNodes());
    } catch (ParserCreationException e) {
      return null;
    }
  }
}
