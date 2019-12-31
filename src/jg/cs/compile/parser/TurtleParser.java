package jg.cs.compile.parser;
/*
 * TurtleParser.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.ProductionPatternAlternative;
import net.percederberg.grammatica.parser.RecursiveDescentParser;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A token stream parser.
 *
 *
 */
public class TurtleParser extends RecursiveDescentParser {

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_1 = 3001;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_2 = 3002;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_3 = 3003;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_4 = 3004;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_5 = 3005;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_6 = 3006;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_7 = 3007;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_8 = 3008;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_9 = 3009;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_10 = 3010;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_11 = 3011;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_12 = 3012;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_13 = 3013;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_14 = 3014;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_15 = 3015;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_16 = 3016;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_17 = 3017;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_18 = 3018;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_19 = 3019;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_20 = 3020;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_21 = 3021;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_22 = 3022;

    /**
     * Creates a new parser with a default analyzer.
     *
     * @param in             the input stream to read from
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public TurtleParser(Reader in) throws ParserCreationException {
        super(in);
        createPatterns();
    }

    /**
     * Creates a new parser.
     *
     * @param in             the input stream to read from
     * @param analyzer       the analyzer to use while parsing
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public TurtleParser(Reader in, TurtleAnalyzer analyzer)
        throws ParserCreationException {

        super(in, analyzer);
        createPatterns();
    }

    /**
     * Creates a new tokenizer for this parser. Can be overridden by a
     * subclass to provide a custom implementation.
     *
     * @param in             the input stream to read from
     *
     * @return the tokenizer created
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    protected Tokenizer newTokenizer(Reader in)
        throws ParserCreationException {

        return new TurtleTokenizer(in);
    }

    /**
     * Initializes the parser by creating all the production patterns.
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        ProductionPattern             pattern;
        ProductionPatternAlternative  alt;

        pattern = new ProductionPattern(TurtleConstants.PROGRAM,
                                        "program");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_1, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.EXPR,
                                        "expr");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_9, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_10, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_11, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_12, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.DATA_DEC,
                                        "dataDec");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.DATA, 1, 1);
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(SUBPRODUCTION_13, 1, -1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.MUTE,
                                        "mute");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.MUTATE, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.RETRIEVE,
                                        "retrieve");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.GET, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addToken(TurtleConstants.NAME, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.FUNC_DEF,
                                        "funcDef");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.DEF, 1, 1);
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addProduction(SUBPRODUCTION_15, 0, -1);
        alt.addToken(TurtleConstants.COLON, 1, 1);
        alt.addProduction(SUBPRODUCTION_16, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.LET_DEC,
                                        "letDec");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.LET, 1, 1);
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(SUBPRODUCTION_18, 1, -1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.IF_EXPR,
                                        "ifExpr");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.IF, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.BIN_OP,
                                        "binOp");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_19, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.SET_VAR,
                                        "setVar");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.SET, 1, 1);
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.FUNC_CALL,
                                        "funcCall");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.WHILE_LOOP,
                                        "whileLoop");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.WHILE, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.NULL_EXPR,
                                        "nullExpr");
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addToken(TurtleConstants.NULL, 1, 1);
        alt.addProduction(TurtleConstants.TYPE, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.TYPE,
                                        "type");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_20, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TurtleConstants.ATOM,
                                        "atom");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_22, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_1,
                                        "Subproduction1");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TurtleConstants.EXPR, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_2,
                                        "Subproduction2");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.FUNC_DEF, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_3,
                                        "Subproduction3");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.LET_DEC, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_4,
                                        "Subproduction4");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.IF_EXPR, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_5,
                                        "Subproduction5");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.BIN_OP, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_6,
                                        "Subproduction6");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.SET_VAR, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_7,
                                        "Subproduction7");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.FUNC_CALL, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_8,
                                        "Subproduction8");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.WHILE_LOOP, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_9,
                                        "Subproduction9");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TurtleConstants.ATOM, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_2, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_3, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_4, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_5, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_6, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_7, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_8, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_10,
                                        "Subproduction10");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.DATA_DEC, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_11,
                                        "Subproduction11");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.MUTE, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_12,
                                        "Subproduction12");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(TurtleConstants.RETRIEVE, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_13,
                                        "Subproduction13");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addToken(TurtleConstants.COLON, 1, 1);
        alt.addProduction(TurtleConstants.TYPE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_14,
                                        "Subproduction14");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addToken(TurtleConstants.COLON, 1, 1);
        alt.addProduction(TurtleConstants.TYPE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_15,
                                        "Subproduction15");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(SUBPRODUCTION_14, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_16,
                                        "Subproduction16");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TurtleConstants.TYPE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.VOID, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_17,
                                        "Subproduction17");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        alt.addToken(TurtleConstants.COLON, 1, 1);
        alt.addProduction(TurtleConstants.TYPE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_18,
                                        "Subproduction18");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.OP_PAREN, 1, 1);
        alt.addProduction(SUBPRODUCTION_17, 1, 1);
        alt.addProduction(TurtleConstants.EXPR, 1, 1);
        alt.addToken(TurtleConstants.CL_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_19,
                                        "Subproduction19");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.PLUS, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.MINUS, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.MULT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.EXPONENT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.LESS, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.GREAT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.EQUAL, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NOT_EQ, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.GR_EQ, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.LS_EQ, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_20,
                                        "Subproduction20");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.INT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.BOOL, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.STR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_21,
                                        "Subproduction21");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.MINUS, 0, 1);
        alt.addToken(TurtleConstants.INTEGER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_22,
                                        "Subproduction22");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_21, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.FALSE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.TRUE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.STRING, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TurtleConstants.NAME, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TurtleConstants.NULL_EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);
    }
}
