package jg.cs.compile.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Stack;

import jg.cs.common.types.Type;
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
import jg.cs.compile.nodes.atoms.Keyword;
import jg.cs.compile.nodes.atoms.NullValue;
import jg.cs.compile.nodes.atoms.Str;
import jg.cs.compile.nodes.atoms.Typ;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class ExprBuilder extends TurtleAnalyzer{

  protected Stack<ArrayDeque<Expr>> stack;
  protected Stack<Expr> actualNodes;

  public ExprBuilder() {
    stack = new Stack<>();
    actualNodes = new Stack<>();
  }

  //Atoms BEGIN
  @Override
  protected Node exitInteger(Token node) throws ParseException {
    actualNodes.push(new Int(node));
    //System.out.println(" --->Enter Integer");
    return node;
  }

  @Override
  protected Node exitString(Token node) throws ParseException {
    actualNodes.push(new Str(node));
    //System.out.println(" --->Enter Str");

    return node;
  }

  @Override
  protected Node exitTrue(Token node) throws ParseException {
    actualNodes.push(new Bool(node));
    //System.out.println(" --->Enter True");

    return node;
  }

  @Override
  protected Node exitFalse(Token node) throws ParseException {
    actualNodes.push(new Bool(node));
    //System.out.println(" --->Enter False");

    return node;
  }

  @Override
  protected Node exitName(Token node) throws ParseException {
    actualNodes.push(new Identifier(node));
    //System.out.println(" --->Enter Name");

    return node;
  }
  
  protected Node exitNull(Token node) throws ParseException{
    actualNodes.push(new NullValue(node, null));  
    //System.out.println("---GOT NULL  ln:"+node.getStartLine()+" | "+actualNodes);
    return node;
  }

  //Atoms END
  
  //Keyword BEGIN
  protected Node exitLet(Token node) throws ParseException{
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitDef(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitIf(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitSet(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitWhile(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitColon(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitGet(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitMutate(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  
  @Override
  protected Node exitData(Token node) throws ParseException {
    actualNodes.push(new Keyword(node));
    return node;
  }
  //Keyword END

  //Types BEGIN
  
  @Override
  protected Node exitType(Production node) throws ParseException {
    if (actualNodes.peek() instanceof Identifier) {
      Identifier identifier = (Identifier) actualNodes.pop();
      actualNodes.push(new Typ(identifier.getLeadToken()));
    }
    
    //System.out.println("----GOT TYPE: "+actualNodes);
    
    return node;
  }
  
  @Override
  protected Node exitBool(Token node) throws ParseException {
    actualNodes.push(new Typ(node));
    return node;
  }

  @Override
  protected Node exitInt(Token node) throws ParseException {
    actualNodes.push(new Typ(node));
    return node;
  }

  @Override
  protected Node exitStr(Token node) throws ParseException {
    actualNodes.push(new Typ(node));
    return node;
  }
  
  @Override
  protected Node exitVoid(Token node) throws ParseException {
    actualNodes.push(new Typ(node));
    return node;
  } 
  //Types END

  //Opertors BEGIN
  @Override
  protected Node exitLess(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }

  @Override
  protected Node exitGreat(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }

  @Override
  protected Node exitEqual(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }

  @Override
  protected Node exitPlus(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }
  
  @Override
  protected Node exitExponent(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }

  @Override
  protected Node exitMult(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }

  @Override
  protected Node exitMinus(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }
  
  @Override
  protected Node exitGrEq(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }
  
  @Override
  protected Node exitLsEq(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }
  
  @Override
  protected Node exitNotEq(Token node) throws ParseException {
    actualNodes.push(new BinaryOperator(node));
    return node;
  }
  //Operators END

  //recursive defs START
  @Override
  protected void enterExpr(Production node) throws ParseException {
    //System.out.println("-----> Entering expr rule <----");
    setEntrance();
  }

  @Override
  protected void enterBinOp(Production node) throws ParseException {
    //System.out.println("-----> Entering Bin op <------");
    setEntrance();
  }
  
  @Override
  protected void enterFuncCall(Production node) throws ParseException {
    //System.out.println("-----> Entering Func Call <------");
    setEntrance();
  }
  
  @Override
  protected void enterLetDec(Production node) throws ParseException {
    //System.out.println("-----> Entering Let Declaration <------");
    setEntrance();
  }
  
  @Override
  protected void enterFuncDef(Production node) throws ParseException {
    //System.out.println("-----> Entering Func Declaration <------");
    setEntrance();
  }
  
  
  @Override
  protected void enterIfExpr(Production node) throws ParseException {
    //System.out.println("-----> Entering If exprs <------");
    setEntrance();
  }
  
  @Override
  protected void enterWhileLoop(Production node) throws ParseException {
    //System.out.println("-----> Entering While exprs <------");
    setEntrance();
  }
  
  @Override
  protected void enterSetVar(Production node) throws ParseException {
    //System.out.println("------> Entering Set Expr <------");
    setEntrance();
  }
  
  @Override
  protected void enterDataDec(Production node) throws ParseException {
    setEntrance();
  }
  
  @Override
  protected void enterMute(Production node) throws ParseException {
    setEntrance();
  }
  
  @Override
  protected void enterRetrieve(Production node) throws ParseException {
    setEntrance();
  }
  
  @Override
  protected void enterNullExpr(Production node) throws ParseException {
    setEntrance();
  }
  
  @Override
  protected Node exitNullExpr(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //get the bare null
    NullValue bareNull = (NullValue) exprs.pollFirst();
    
    //get type of null
    Typ type = (Typ) exprs.pollFirst();
    
    //nulls cannot be of primitive type
    if (Type.PRIMITIVE.containsKey(type.getActualValue().getName())) {
      throw new ParseException(ParseException.INTERNAL_ERROR, 
          "Null values cannot be of a primitive type!", 
          bareNull.getLeadLnNumber(), 
          bareNull.getLeadColNumber());
    }
    
    actualNodes.push(new NullValue(bareNull.getLeadToken(), type));


    //System.out.println(exprs);
    return node;
  }
  
  @Override
  protected Node exitRetrieve(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is the "get" keyword
    Keyword keyword = (Keyword) exprs.pollFirst();
    
    //second expr is the target of the mutation
    Expr target = exprs.pollFirst();
    
    //third expr is the identifier which identifies the member name to retrieve
    Identifier memberName = (Identifier) exprs.pollFirst();

    RetrieveExpr retrieveExpr = new RetrieveExpr(keyword.getLeadToken(), target, memberName);
    
    //System.out.println("---> Exit SET : "+set);
    
    actualNodes.push(retrieveExpr);
    return node;
  }
  
  @Override
  protected Node exitMute(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is the "mut" keyword
    Keyword keyword = (Keyword) exprs.pollFirst();
    
    //second expr is the target of the mutation
    Expr target = exprs.pollFirst();
    
    //third expr is the identifier which identifies the member name to change
    Identifier memberName = (Identifier) exprs.pollFirst();
    
    //fourth expr is the new value
    Expr newValue = exprs.pollFirst();

    MutateExpr mutateExpr = new MutateExpr(keyword.getLeadToken(), 
        target, 
        memberName, 
        newValue);
    
    //System.out.println("---> Exit SET : "+set);
    
    actualNodes.push(mutateExpr);
    return node;
  }
  
  @Override
  protected Node exitDataDec(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is the "data" keyword
    Keyword dataKeyword = (Keyword) exprs.pollFirst();
    
    //second expr is the name of the data declaration
    Identifier identifier = (Identifier) exprs.pollFirst();
    
    //member map
    LinkedHashMap<Identifier, Typ> memberMap = new LinkedHashMap<>();
    
    while (!exprs.isEmpty()) {
      Identifier memberName = (Identifier) exprs.pollFirst();
      Keyword colon = (Keyword) exprs.pollFirst();
      Typ memberType = (Typ) exprs.pollFirst();
      
      if (memberMap.containsKey(memberName)) {
        throw new ParseException(ParseException.INTERNAL_ERROR, 
            "The variable "+memberName.getActualValue()+" has already been declared!", 
            memberName.getLeadLnNumber(), 
            memberName.getLeadColNumber());
      }
      else {
        memberMap.put(memberName, memberType);
      }
    }
    
    DataDeclaration declaration = new DataDeclaration(dataKeyword.getLeadToken(), 
        identifier.getLeadToken(), memberMap);
    actualNodes.push(declaration);
    
    return node;
  }
  
  @Override
  protected Node exitSetVar(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is the "set" keyword
    Keyword setKeyword = (Keyword) exprs.pollFirst();
    
    //second expr is the identifier of the variable
    Identifier identifier = (Identifier) exprs.pollFirst();
    
    //third expr is the value to set the variable
    Expr value = exprs.pollFirst();

    SetExpr set = new SetExpr(setKeyword.getLeadToken(), 
        identifier, 
        value);
    
    //System.out.println("---> Exit SET : "+set);
    
    actualNodes.push(set);
    return node;
  }
  
  @Override
  protected Node exitWhileLoop(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is actually the "while" keyword
    Keyword whileKeyWord = (Keyword) exprs.pollFirst();
    
    //second expr is the condition to check
    Expr condition = exprs.pollFirst();
    
    //rest of expressions is the body of the while loop
    ArrayList<Expr> statements = new ArrayList<>();
    while (!exprs.isEmpty()) {
      statements.add(exprs.pollFirst());
    }
    
    WhileExpr whileLoop = new WhileExpr(whileKeyWord.getLeadToken()
        , condition, statements);
    
    //System.out.println("-----> Exiting While: "+whileLoop);
    actualNodes.push(whileLoop);
    return node;
  }
  
  @Override
  protected Node exitIfExpr(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is actually the "if" keyword
    Keyword ifKeyword = (Keyword) exprs.pollFirst();
    
    //second expr is the condition to check
    Expr condition = exprs.pollFirst();
    
    //third expr is the true consequence
    Expr trueConseq = exprs.pollFirst();
    
    //fourth expr is the false consequence
    Expr falseConseq = exprs.pollFirst();

    IfExpr ifExpr = new IfExpr(ifKeyword.getLeadToken(), 
        condition, 
        trueConseq, 
        falseConseq);
    
    actualNodes.push(ifExpr);
    //System.out.println("-----> Exiting If: "+ifExpr);
    
    return node;
  }
  
  @Override
  protected Node exitFuncDef(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();

    //first expr is actually the "def" keyword
    Keyword defKeyword = (Keyword) exprs.pollFirst();
    
    //second expr is the function name
    Identifier funcName = (Identifier) exprs.pollFirst();
    
    LinkedHashMap<String, IdenTypeValTuple> parameters = new LinkedHashMap<>();
    //will have to manually gather let variables
    //by capturing three exprs at a time: name, type, and expr
    
    while (!exprs.isEmpty()) {
      Expr name = exprs.pollFirst();
      Expr potentialColon = exprs.pollFirst();
      if (name instanceof Typ) {
        //if name is a keyword, then this function must have no parameters
        exprs.addFirst(name);
        exprs.addFirst(potentialColon);
        break;
      }
      else if (potentialColon == null) {
        exprs.addFirst(name);
        break;
      }
      else if (potentialColon instanceof Keyword && 
          ((Keyword) potentialColon).getActualValue().equals(":")) {
        Identifier actualName = (Identifier) name;
        Typ varType = (Typ) exprs.pollFirst();
        
        if (parameters.containsKey(actualName.getActualValue())) {
          throw new ParseException(ParseException.INTERNAL_ERROR, 
              "The variable "+actualName.getActualValue()+" has already been declared!", 
              actualName.getLeadLnNumber(), 
              actualName.getLeadColNumber());
        }
        parameters.put(actualName.getActualValue(), new IdenTypeValTuple(actualName, 
            varType.getActualValue()));
      }
      else {
        exprs.addFirst(potentialColon);
        exprs.addFirst(name);
        break;
      } 
    }
    
    //Gets rid of the colon keyword
    exprs.pollFirst();
        
    //function return type
    Typ functionReturn = (Typ) exprs.pollFirst();
    
    //the rest of the expressions are the sequential statements
    ArrayList<Expr> statements = new ArrayList<>();
    while (!exprs.isEmpty()) {
      statements.add(exprs.pollFirst());
    }
    
    FunctDefExpr functDefExpr = new FunctDefExpr(defKeyword.getLeadToken(), 
        funcName.getLeadToken(), 
        parameters, 
        functionReturn, 
        statements);
    
    //System.out.println("----> Exiting Function DEF: "+functDefExpr);
    
    actualNodes.push(functDefExpr);
    
    return node;
  }
    
  @Override
  protected Node exitLetDec(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first expr is actually the "let" keyword
    Keyword letKeyWord = (Keyword) exprs.pollFirst();
    
    LinkedHashMap<String, IdenTypeValTuple> letVariables = new LinkedHashMap<>();
    //will have to manually gather let variables
    
    //System.out.println(exprs);
    
    while (!exprs.isEmpty()) {
      Expr name = exprs.pollFirst();
      Expr potentialColon = exprs.pollFirst();
      if (potentialColon == null) {
        exprs.addFirst(name);
        break;
      }
      else if (potentialColon instanceof Keyword && 
          ((Keyword) potentialColon).getActualValue().equals(":")) {
        Identifier actualName = (Identifier) name;
        Typ varType = (Typ) exprs.pollFirst();
        Expr value = exprs.pollFirst();
        
        if (letVariables.containsKey(actualName.getActualValue())) {
          throw new ParseException(ParseException.INTERNAL_ERROR, 
              "The variable "+actualName.getActualValue()+" has already been declared!", 
              actualName.getLeadLnNumber(), 
              actualName.getLeadColNumber());
        }
        letVariables.put(actualName.getActualValue(), new IdenTypeValTuple(actualName, 
            varType.getActualValue(), 
            value));
      }
      else {
        exprs.addFirst(potentialColon);
        exprs.addFirst(name);
        break;
      }      
    }
    
    //the rest of the expressions are the sequential statements
    ArrayList<Expr> statements = new ArrayList<>();
    while (!exprs.isEmpty()) {
      statements.add(exprs.pollFirst());
    }
    
    //now push let object to stack
    LetExpr letExpr = new LetExpr(letKeyWord.getLeadToken(), letVariables, statements);
    //System.out.println("-----> EXITING Let Declaration: "+letExpr);
    actualNodes.push(letExpr);
    return node;
  }
  
  @Override
  protected Node exitFuncCall(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    
    //first element if function name
    Identifier funcName = (Identifier) exprs.pollFirst();
    
    ArrayList<Expr> arguments = new ArrayList<>();
    //rest of exprs are function arguments
    while (!exprs.isEmpty()) {
      arguments.add(exprs.pollFirst());
    }
    
    FunctionCall functionCall = new FunctionCall(funcName.getLeadToken(), arguments);
    actualNodes.push(functionCall);
    //System.out.println("-----> Exiting Func Call : "+functionCall);
    return node;
  }

  @Override
  protected Node exitBinOp(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();

    //first element is operator
    BinaryOperator operator = (BinaryOperator) exprs.pollFirst();
    
    //next two elements are left and right operators
    Expr left = exprs.pollFirst();
    Expr right = exprs.pollFirst();

    actualNodes.push(new BinaryOpExpr(operator, left, right));
    //System.out.println("-----> Exiting BinOp <------");
    return node;
  }

  @Override
  protected Node exitExpr(Production node) throws ParseException {
    ArrayDeque<Expr> exprs = exitEntrance();
    //System.out.println(actualNodes+" | "+exprs);
    actualNodes.push(exprs.pop());
    //System.out.println("----> EXITING EXPR <-----");
    return node;
  }
  //recursive defs END

  //HELPER methods start
  private void setEntrance(){
    stack.add(new ArrayDeque<>());
    actualNodes.push(null); //add marker
  }

  private ArrayDeque<Expr> exitEntrance(){
    ArrayDeque<Expr> latest = stack.pop();

    while (actualNodes.peek() != null) {
      latest.addFirst(actualNodes.pop());
    }

    actualNodes.pop(); //removes marker

    return latest;
  }
  //HELPER methods DONE

  /**
   * Resets this Analyzer for reuse with a different source
   */
  public void reset() {
    stack.clear();
    actualNodes.clear();
  }

  public Stack<Expr> getStackNodes() {
    return actualNodes;
  }
}
