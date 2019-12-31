package jg.cs.compile.parser;
/*
 * TurtleTokenizer.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.TokenPattern;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A character stream tokenizer.
 *
 *
 */
public class TurtleTokenizer extends Tokenizer {

    /**
     * Creates a new tokenizer for the specified input stream.
     *
     * @param input          the input stream to read
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    public TurtleTokenizer(Reader input)
        throws ParserCreationException {

        super(input, false);
        createPatterns();
    }

    /**
     * Initializes the tokenizer by creating all the token patterns.
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        TokenPattern  pattern;

        pattern = new TokenPattern(TurtleConstants.WHITESPACE,
                                   "WHITESPACE",
                                   TokenPattern.REGEXP_TYPE,
                                   "[ \\t\\n\\r]+");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.STRING,
                                   "STRING",
                                   TokenPattern.REGEXP_TYPE,
                                   "\\'.*?\\'");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.INTEGER,
                                   "INTEGER",
                                   TokenPattern.REGEXP_TYPE,
                                   "([-|+]?[0-9]+)");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.TRUE,
                                   "TRUE",
                                   TokenPattern.STRING_TYPE,
                                   "true");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.FALSE,
                                   "FALSE",
                                   TokenPattern.STRING_TYPE,
                                   "false");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.NULL,
                                   "NULL",
                                   TokenPattern.STRING_TYPE,
                                   "null");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.WHILE,
                                   "WHILE",
                                   TokenPattern.STRING_TYPE,
                                   "while");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.IF,
                                   "IF",
                                   TokenPattern.STRING_TYPE,
                                   "if");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.INT,
                                   "INT",
                                   TokenPattern.STRING_TYPE,
                                   "int");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.STR,
                                   "STR",
                                   TokenPattern.STRING_TYPE,
                                   "string");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.BOOL,
                                   "BOOL",
                                   TokenPattern.STRING_TYPE,
                                   "bool");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.VOID,
                                   "VOID",
                                   TokenPattern.STRING_TYPE,
                                   "void");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.LET,
                                   "LET",
                                   TokenPattern.STRING_TYPE,
                                   "let");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.DEF,
                                   "DEF",
                                   TokenPattern.STRING_TYPE,
                                   "def");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.SET,
                                   "SET",
                                   TokenPattern.STRING_TYPE,
                                   "set");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.DATA,
                                   "DATA",
                                   TokenPattern.STRING_TYPE,
                                   "data");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.MUTATE,
                                   "MUTATE",
                                   TokenPattern.STRING_TYPE,
                                   "mut");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.GET,
                                   "GET",
                                   TokenPattern.STRING_TYPE,
                                   "get");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.NAME,
                                   "NAME",
                                   TokenPattern.REGEXP_TYPE,
                                   "[a-zA-Z][a-zA-Z0-9_]*");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.NOT_EQ,
                                   "NOT_EQ",
                                   TokenPattern.STRING_TYPE,
                                   "!=");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.GR_EQ,
                                   "GR_EQ",
                                   TokenPattern.STRING_TYPE,
                                   ">=");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.LS_EQ,
                                   "LS_EQ",
                                   TokenPattern.STRING_TYPE,
                                   "<=");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.PLUS,
                                   "PLUS",
                                   TokenPattern.STRING_TYPE,
                                   "+");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.MINUS,
                                   "MINUS",
                                   TokenPattern.STRING_TYPE,
                                   "-");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.MULT,
                                   "MULT",
                                   TokenPattern.STRING_TYPE,
                                   "*");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.LESS,
                                   "LESS",
                                   TokenPattern.STRING_TYPE,
                                   "<");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.GREAT,
                                   "GREAT",
                                   TokenPattern.STRING_TYPE,
                                   ">");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.EQUAL,
                                   "EQUAL",
                                   TokenPattern.STRING_TYPE,
                                   "=");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.COLON,
                                   "COLON",
                                   TokenPattern.STRING_TYPE,
                                   ":");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.EXPONENT,
                                   "EXPONENT",
                                   TokenPattern.STRING_TYPE,
                                   "^");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.OP_PAREN,
                                   "OP_PAREN",
                                   TokenPattern.STRING_TYPE,
                                   "(");
        addPattern(pattern);

        pattern = new TokenPattern(TurtleConstants.CL_PAREN,
                                   "CL_PAREN",
                                   TokenPattern.STRING_TYPE,
                                   ")");
        addPattern(pattern);
    }
}
