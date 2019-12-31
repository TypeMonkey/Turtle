package jg.cs.compile.parser;

/*
 * TurtleAnalyzer.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

import net.percederberg.grammatica.parser.Analyzer;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

/**
 * A class providing callback methods for the parser.
 *
 *
 */
public abstract class TurtleAnalyzer extends Analyzer {

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enter(Node node) throws ParseException {
        switch (node.getId()) {
        case TurtleConstants.STRING:
            enterString((Token) node);
            break;
        case TurtleConstants.INTEGER:
            enterInteger((Token) node);
            break;
        case TurtleConstants.TRUE:
            enterTrue((Token) node);
            break;
        case TurtleConstants.FALSE:
            enterFalse((Token) node);
            break;
        case TurtleConstants.NULL:
            enterNull((Token) node);
            break;
        case TurtleConstants.WHILE:
            enterWhile((Token) node);
            break;
        case TurtleConstants.IF:
            enterIf((Token) node);
            break;
        case TurtleConstants.INT:
            enterInt((Token) node);
            break;
        case TurtleConstants.STR:
            enterStr((Token) node);
            break;
        case TurtleConstants.BOOL:
            enterBool((Token) node);
            break;
        case TurtleConstants.VOID:
            enterVoid((Token) node);
            break;
        case TurtleConstants.LET:
            enterLet((Token) node);
            break;
        case TurtleConstants.DEF:
            enterDef((Token) node);
            break;
        case TurtleConstants.SET:
            enterSet((Token) node);
            break;
        case TurtleConstants.DATA:
            enterData((Token) node);
            break;
        case TurtleConstants.MUTATE:
            enterMutate((Token) node);
            break;
        case TurtleConstants.GET:
            enterGet((Token) node);
            break;
        case TurtleConstants.NAME:
            enterName((Token) node);
            break;
        case TurtleConstants.NOT_EQ:
            enterNotEq((Token) node);
            break;
        case TurtleConstants.GR_EQ:
            enterGrEq((Token) node);
            break;
        case TurtleConstants.LS_EQ:
            enterLsEq((Token) node);
            break;
        case TurtleConstants.PLUS:
            enterPlus((Token) node);
            break;
        case TurtleConstants.MINUS:
            enterMinus((Token) node);
            break;
        case TurtleConstants.MULT:
            enterMult((Token) node);
            break;
        case TurtleConstants.LESS:
            enterLess((Token) node);
            break;
        case TurtleConstants.GREAT:
            enterGreat((Token) node);
            break;
        case TurtleConstants.EQUAL:
            enterEqual((Token) node);
            break;
        case TurtleConstants.COLON:
            enterColon((Token) node);
            break;
        case TurtleConstants.EXPONENT:
            enterExponent((Token) node);
            break;
        case TurtleConstants.OP_PAREN:
            enterOpParen((Token) node);
            break;
        case TurtleConstants.CL_PAREN:
            enterClParen((Token) node);
            break;
        case TurtleConstants.PROGRAM:
            enterProgram((Production) node);
            break;
        case TurtleConstants.EXPR:
            enterExpr((Production) node);
            break;
        case TurtleConstants.DATA_DEC:
            enterDataDec((Production) node);
            break;
        case TurtleConstants.MUTE:
            enterMute((Production) node);
            break;
        case TurtleConstants.RETRIEVE:
            enterRetrieve((Production) node);
            break;
        case TurtleConstants.FUNC_DEF:
            enterFuncDef((Production) node);
            break;
        case TurtleConstants.LET_DEC:
            enterLetDec((Production) node);
            break;
        case TurtleConstants.IF_EXPR:
            enterIfExpr((Production) node);
            break;
        case TurtleConstants.BIN_OP:
            enterBinOp((Production) node);
            break;
        case TurtleConstants.SET_VAR:
            enterSetVar((Production) node);
            break;
        case TurtleConstants.FUNC_CALL:
            enterFuncCall((Production) node);
            break;
        case TurtleConstants.WHILE_LOOP:
            enterWhileLoop((Production) node);
            break;
        case TurtleConstants.NULL_EXPR:
            enterNullExpr((Production) node);
            break;
        case TurtleConstants.TYPE:
            enterType((Production) node);
            break;
        case TurtleConstants.ATOM:
            enterAtom((Production) node);
            break;
        }
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exit(Node node) throws ParseException {
        switch (node.getId()) {
        case TurtleConstants.STRING:
            return exitString((Token) node);
        case TurtleConstants.INTEGER:
            return exitInteger((Token) node);
        case TurtleConstants.TRUE:
            return exitTrue((Token) node);
        case TurtleConstants.FALSE:
            return exitFalse((Token) node);
        case TurtleConstants.NULL:
            return exitNull((Token) node);
        case TurtleConstants.WHILE:
            return exitWhile((Token) node);
        case TurtleConstants.IF:
            return exitIf((Token) node);
        case TurtleConstants.INT:
            return exitInt((Token) node);
        case TurtleConstants.STR:
            return exitStr((Token) node);
        case TurtleConstants.BOOL:
            return exitBool((Token) node);
        case TurtleConstants.VOID:
            return exitVoid((Token) node);
        case TurtleConstants.LET:
            return exitLet((Token) node);
        case TurtleConstants.DEF:
            return exitDef((Token) node);
        case TurtleConstants.SET:
            return exitSet((Token) node);
        case TurtleConstants.DATA:
            return exitData((Token) node);
        case TurtleConstants.MUTATE:
            return exitMutate((Token) node);
        case TurtleConstants.GET:
            return exitGet((Token) node);
        case TurtleConstants.NAME:
            return exitName((Token) node);
        case TurtleConstants.NOT_EQ:
            return exitNotEq((Token) node);
        case TurtleConstants.GR_EQ:
            return exitGrEq((Token) node);
        case TurtleConstants.LS_EQ:
            return exitLsEq((Token) node);
        case TurtleConstants.PLUS:
            return exitPlus((Token) node);
        case TurtleConstants.MINUS:
            return exitMinus((Token) node);
        case TurtleConstants.MULT:
            return exitMult((Token) node);
        case TurtleConstants.LESS:
            return exitLess((Token) node);
        case TurtleConstants.GREAT:
            return exitGreat((Token) node);
        case TurtleConstants.EQUAL:
            return exitEqual((Token) node);
        case TurtleConstants.COLON:
            return exitColon((Token) node);
        case TurtleConstants.EXPONENT:
            return exitExponent((Token) node);
        case TurtleConstants.OP_PAREN:
            return exitOpParen((Token) node);
        case TurtleConstants.CL_PAREN:
            return exitClParen((Token) node);
        case TurtleConstants.PROGRAM:
            return exitProgram((Production) node);
        case TurtleConstants.EXPR:
            return exitExpr((Production) node);
        case TurtleConstants.DATA_DEC:
            return exitDataDec((Production) node);
        case TurtleConstants.MUTE:
            return exitMute((Production) node);
        case TurtleConstants.RETRIEVE:
            return exitRetrieve((Production) node);
        case TurtleConstants.FUNC_DEF:
            return exitFuncDef((Production) node);
        case TurtleConstants.LET_DEC:
            return exitLetDec((Production) node);
        case TurtleConstants.IF_EXPR:
            return exitIfExpr((Production) node);
        case TurtleConstants.BIN_OP:
            return exitBinOp((Production) node);
        case TurtleConstants.SET_VAR:
            return exitSetVar((Production) node);
        case TurtleConstants.FUNC_CALL:
            return exitFuncCall((Production) node);
        case TurtleConstants.WHILE_LOOP:
            return exitWhileLoop((Production) node);
        case TurtleConstants.NULL_EXPR:
            return exitNullExpr((Production) node);
        case TurtleConstants.TYPE:
            return exitType((Production) node);
        case TurtleConstants.ATOM:
            return exitAtom((Production) node);
        }
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void child(Production node, Node child)
        throws ParseException {

        switch (node.getId()) {
        case TurtleConstants.PROGRAM:
            childProgram(node, child);
            break;
        case TurtleConstants.EXPR:
            childExpr(node, child);
            break;
        case TurtleConstants.DATA_DEC:
            childDataDec(node, child);
            break;
        case TurtleConstants.MUTE:
            childMute(node, child);
            break;
        case TurtleConstants.RETRIEVE:
            childRetrieve(node, child);
            break;
        case TurtleConstants.FUNC_DEF:
            childFuncDef(node, child);
            break;
        case TurtleConstants.LET_DEC:
            childLetDec(node, child);
            break;
        case TurtleConstants.IF_EXPR:
            childIfExpr(node, child);
            break;
        case TurtleConstants.BIN_OP:
            childBinOp(node, child);
            break;
        case TurtleConstants.SET_VAR:
            childSetVar(node, child);
            break;
        case TurtleConstants.FUNC_CALL:
            childFuncCall(node, child);
            break;
        case TurtleConstants.WHILE_LOOP:
            childWhileLoop(node, child);
            break;
        case TurtleConstants.NULL_EXPR:
            childNullExpr(node, child);
            break;
        case TurtleConstants.TYPE:
            childType(node, child);
            break;
        case TurtleConstants.ATOM:
            childAtom(node, child);
            break;
        }
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterString(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitString(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterInteger(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitInteger(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterTrue(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitTrue(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterFalse(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitFalse(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterNull(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitNull(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterWhile(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitWhile(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterIf(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitIf(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterInt(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitInt(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterStr(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitStr(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterBool(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitBool(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterVoid(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitVoid(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterLet(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitLet(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterDef(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitDef(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterSet(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitSet(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterData(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitData(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterMutate(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitMutate(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterGet(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitGet(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterName(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitName(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterNotEq(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitNotEq(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterGrEq(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitGrEq(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterLsEq(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitLsEq(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterPlus(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitPlus(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterMinus(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitMinus(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterMult(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitMult(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterLess(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitLess(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterGreat(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitGreat(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterEqual(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitEqual(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterColon(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitColon(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterExponent(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitExponent(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterOpParen(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitOpParen(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterClParen(Token node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitClParen(Token node) throws ParseException {
        return node;
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterProgram(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitProgram(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childProgram(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterExpr(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitExpr(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childExpr(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterDataDec(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitDataDec(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childDataDec(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterMute(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitMute(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childMute(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterRetrieve(Production node)
        throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitRetrieve(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childRetrieve(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterFuncDef(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitFuncDef(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childFuncDef(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterLetDec(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitLetDec(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childLetDec(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterIfExpr(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitIfExpr(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childIfExpr(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterBinOp(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitBinOp(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childBinOp(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterSetVar(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitSetVar(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childSetVar(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterFuncCall(Production node)
        throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitFuncCall(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childFuncCall(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterWhileLoop(Production node)
        throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitWhileLoop(Production node)
        throws ParseException {

        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childWhileLoop(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterNullExpr(Production node)
        throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitNullExpr(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childNullExpr(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterType(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitType(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childType(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }

    /**
     * Called when entering a parse tree node.
     *
     * @param node           the node being entered
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void enterAtom(Production node) throws ParseException {
    }

    /**
     * Called when exiting a parse tree node.
     *
     * @param node           the node being exited
     *
     * @return the node to add to the parse tree, or
     *         null if no parse tree should be created
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected Node exitAtom(Production node) throws ParseException {
        return node;
    }

    /**
     * Called when adding a child to a parse tree node.
     *
     * @param node           the parent node
     * @param child          the child node, or null
     *
     * @throws ParseException if the node analysis discovered errors
     */
    protected void childAtom(Production node, Node child)
        throws ParseException {

        node.addChild(child);
    }
}
