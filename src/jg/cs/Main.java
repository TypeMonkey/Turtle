package jg.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import jg.cs.compile.Program;
import jg.cs.compile.StructureVerifier;
import jg.cs.compile.TypeChecker;
import jg.cs.compile.nodes.Expr;
import jg.cs.compile.parser.ExprBuilder;
import jg.cs.compile.parser.TurtleParser;
import jg.cs.compile.parser.TurtleTokenizer;
import jg.cs.inter.IRCompiler;
import jg.cs.inter.Result;
import jg.cs.inter.instruction.Instr;
import jg.cs.runtime.Executor;
import jg.cs.runtime.errors.ExecException;
import jg.cs.runtime.values.Value;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;
import net.percederberg.grammatica.parser.Token;
import net.percederberg.grammatica.parser.Tokenizer;

public class Main {

  /**
   * Main driver method for the sNEK interpreter
   * @param args - the string arguments to the interpreter
   */
  public static void main(String[] args) {
    args = new String[1];
    args[0] = "sample.t";
    
    if (args.length == 1 && getFileExtension(args[0]).equals("t")) {
      File targetFile = new File(args[0]);
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
        
        IRCompiler compiler = new IRCompiler(program);
        Result result = compiler.compile();
        
        Arrays.asList(result.getInstructions()).forEach(x -> System.out.println(x == null ? "" : (x.getLineNumber()+" | "+x)));
        
        /*
        //System.out.println("-------------EXECUTING-------------");
        //Executor executor = new Executor(program);
        try {
          //Value<?> result = executor.execute();
        } catch (ExecException e) {
          System.err.println(e.getMessage());
          System.err.println("Exiting......");
          System.exit(-1);
        }
        
        //System.out.println(" -----> FINAL: "+result);
         */
      }
      else {
        System.err.println("turtle: Provided source file is either nonexistant or unreadble!");
      }
    }
    else {
      System.err.println("turle: Missing .t file as argument!");
    }
  }
  
  public static Options getCommandLineOptions() {
    Options options = new Options();
    
    Option help = new Option("h", "Lists command options for the Turtle interpreter");
    help.setLongOpt("help");
    help.setArgs(0);
    
    Option stackOnDisk = new Option("s", "Will load the stack on to disk");
    stackOnDisk.setLongOpt("sload");
    stackOnDisk.setArgs(0);
    
    Option heapOnDisk = new Option("h", "Will load heap on to disk");
    stackOnDisk.setLongOpt("hload");
    stackOnDisk.setArgs(0);
    
    Option heapSize = new Option("max", "Sets the max size of the heap, in bytes");
    stackOnDisk.setLongOpt("hload");
    stackOnDisk.setArgs(0);
    
    options.addOption(help);
    options.addOption(stackOnDisk);
    options.addOption(heapOnDisk);
    options.addOption(heapSize);
    
    return options;
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
      
      String allValidLines = nonCommentLines.stream().collect(Collectors.joining(System.lineSeparator()));
      
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
