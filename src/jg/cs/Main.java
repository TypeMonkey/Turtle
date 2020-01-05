package jg.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

        //System.out.println("----EXPRS");
        //components.forEach(x -> System.out.println(x));
        //System.out.println("----EXPRS");
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
        PrintWriter writer = new PrintWriter(clOptions.getValue(CLOption.IR_OUTPUT));
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
      
      //MemOperandStack operandStack = new MemOperandStack();       
      //MemHeapAllocator heapAllocator = new MemHeapAllocator(1000);
      //MemFunctionStack functionStack = new MemFunctionStack();


      OperandStack operandStack = clOptions.getValue(CLOption.DISK_OP_STACK) != null ?
                                   new DiskOperandStack(new File(clOptions.getValue(CLOption.DISK_OP_STACK))) :
                                   new MemOperandStack();
                                   
      HeapAllocator heapAllocator = clOptions.getValue(CLOption.DISK_HEAP) != null ?
                                       new DiskHeapAllocator(new File(clOptions.getValue(CLOption.DISK_HEAP)), clOptions.getMaxHeapSize()) :
                                       new MemHeapAllocator((int) clOptions.getMaxHeapSize());
                                       
      FunctionStack functionStack = clOptions.getValue(CLOption.DISK_F_STACK) != null ?
                                       new DiskFunctionStack(new File(clOptions.getValue(CLOption.DISK_F_STACK))) :
                                       new MemFunctionStack();

      Executor executor = new Executor(functionStack, 
          operandStack, heapAllocator, result);
      executor.init();

      //System.out.println("-------EXECUTING!!!!-------  MAX HEAP: "+heapAllocator.getMaxSpace());
      try {
        executor.execute();
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
    
    Option ostack = new Option("o", "Will load the operand stack on to disk, using the provided file to write data to.");
    ostack.setLongOpt("oload");
    ostack.setArgs(1);
    ostack.setRequired(false);
    
    Option fstack = new Option("s", "Will load the function stack on to disk, using the provided file to write data to.");
    fstack.setLongOpt("sload");
    fstack.setArgs(1);
    fstack.setRequired(false);
    
    Option heapOnDisk = new Option("e", "Will load heap on to disk, using the provided file to write data to.");
    heapOnDisk.setLongOpt("hload");
    heapOnDisk.setArgs(1);
    heapOnDisk.setRequired(false);
    
    Option heapSize = new Option("m", "Sets the max size of the heap, in bytes");
    heapSize.setLongOpt("max");
    heapSize.setArgs(1);
    heapSize.setRequired(false);
    
    Option irOutput = new Option("i", "Outputs the IR assembly code to a file. Helpful for debugging.");
    irOutput.setLongOpt("irout");
    irOutput.setArgs(1);
    irOutput.setRequired(false);
    
    options.addOption(help);
    options.addOption(fstack);
    options.addOption(ostack);
    options.addOption(heapOnDisk);
    options.addOption(heapSize);
    options.addOption(irOutput);
    
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
      long maxHeap = 1600;
      
      if (commandLine.hasOption("o") | commandLine.hasOption("oload")) {
        optionMap.put(CLOption.DISK_OP_STACK, commandLine.hasOption("o") ? 
                                              commandLine.getOptionValue("o") : 
                                              commandLine.getOptionValue("oload"));
        //System.out.println("---DISK OP STACK");
      }
      if (commandLine.hasOption("s") | commandLine.hasOption("sload")) {
        optionMap.put(CLOption.DISK_F_STACK, commandLine.hasOption("s") ? 
            commandLine.getOptionValue("s") : 
            commandLine.getOptionValue("sload"));
        //System.out.println("---DISK FUNC STACK "+optionMap);
      }
      if (commandLine.hasOption("e") | commandLine.hasOption("hload")) {
        optionMap.put(CLOption.DISK_HEAP, commandLine.hasOption("e") ? 
            commandLine.getOptionValue("e") : 
            commandLine.getOptionValue("hload"));
        //System.out.println("---DISK HEAP");
      }
      if (commandLine.hasOption("i") | commandLine.hasOption("irout")) {
        optionMap.put(CLOption.IR_OUTPUT, commandLine.hasOption("i") ? 
            commandLine.getOptionValue("i") : 
            commandLine.getOptionValue("irout"));
        //System.out.println("---DISK IR OUTPUT");
      }
      
      if (commandLine.hasOption("m") | commandLine.hasOption("max")) {
        String rawMax = commandLine.hasOption("m") ? commandLine.getOptionValue("m") : commandLine.getOptionValue("max");
        
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
        
        return new CommandLineOption(maxHeap, optionMap, sourceFile);
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
      
      System.out.println("----------VALIDS:");
      System.out.println(allValidLines);
      
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
