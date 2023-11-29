/*  (c) 2004 Allen I. Holub. All rights reserved.
 *
 *  This code may be used freely by yourself with the following
 *  restrictions:
 *
 *  o Your splash screen, about box, or equivalent, must include
 *    Allen Holub's name, copyright, and URL. For example:
 *
 *      This program contains Allen Holub's SQL package.<br>
 *      (c) 2005 Allen I. Holub. All Rights Reserved.<br>
 *              http://www.holub.com<br>
 *
 *    If your program does not run interactively, then the foregoing
 *    notice must appear in your documentation.
 *
 *  o You may not redistribute (or mirror) the source code.
 *
 *  o You must report any bugs that you find to me. Use the form at
 *    http://www.holub.com/company/contact.html or send email to
 *    allen@Holub.com.
 *
 *  o The software is supplied <em>as is</em>. Neither Allen Holub nor
 *    Holub Associates are responsible for any bugs (or any problems
 *    caused by bugs, including lost productivity or data)
 *    in any of this code.
 */
package mobile.shop.holub.sqlengine.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import mobile.shop.holub.sqlengine.enums.TokenType;


public class Scanner {
    private Token currentToken = new BeginToken();
    private BufferedReader inputReader = null;
    private int inputLineNumber = 0;
    private String inputLine = null;
    private int inputPosition = 0;


    /**
     * Create a Scanner for the indicated token set, which will get input from the indicated string.
     */
    public Scanner(String input) {
        this(new StringReader(input));
    }

    /**
     * Create a Scanner for the indicated token set, which will get input from the indicated Reader.
     */
    public Scanner(Reader inputReader) {
        this.inputReader =
                (inputReader instanceof BufferedReader)
                        ? (BufferedReader) inputReader
                        : new BufferedReader(inputReader)
        ;
        loadLine();
    }

    /**
     * Load the next input line and adjust the line number and inputPosition offset.
     */
    private boolean loadLine() {
        try {
            inputLine = inputReader.readLine();
            if (inputLine != null) {
                ++inputLineNumber;
                inputPosition = 0;
            }
            return inputLine != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Return true if the current token matches the candidate token.
     */
    public boolean match(TokenType candidate) {
        return currentToken == candidate.getToken();
    }

    /**
     * Advance the input to the next token and return the current token (the one in the input before the advance). This
     * returned token is valid only until the next <code>advance()</code> call (at which time the lexeme may change, for
     * example).
     */
    public Token advance() throws ParseFailure {
        try {
            if (currentToken != null)    // not at end of file
            {
                inputPosition += currentToken.lexeme().length();
                currentToken = null;

                if (inputPosition == inputLine.length()) {
                    if (!loadLine()) {
                        return null;
                    }
                }

                while (Character.isWhitespace(
                        inputLine.charAt(inputPosition))) {
                    if (++inputPosition == inputLine.length()) {
                        if (!loadLine()) {
                            return null;
                        }
                    }
                }

                for (TokenType tokenType : TokenType.values()) {
                    Token token = tokenType.getToken();
                    if (token.match(inputLine, inputPosition)) {
                        this.currentToken = token;
                        break;
                    }
                }

                if (currentToken == null) {
                    throw failure("Unrecognized Input");
                }
            }
        } catch (IndexOutOfBoundsException e) { /* nothing to do */ }
        return currentToken;
    }


    public ParseFailure failure(String message) {
        return new ParseFailure(message,
                inputLine, inputPosition, inputLineNumber);
    }

    /**
     * Combines the match and advance operations. Advance automatically if the match occurs.
     *
     * @return the lexeme if there was a match and the input was advanced, null if there was no match (the input is not
     * advanced).
     */
    public String matchAdvance(TokenType candidate) throws ParseFailure {
        if (match(candidate)) {
            String lexeme = currentToken.lexeme();
            advance();
            return lexeme;
        }
        return null;
    }

    /**
     * If the specified candidate is the current token, advance past it and return the lexeme; otherwise, throw an
     * exception with the rror message "XXX Expected".
     *
     * @throws ParseFailure if the required token isn't the current token.
     */
    public final String required(TokenType candidate) throws ParseFailure {
        String lexeme = matchAdvance(candidate);
        if (lexeme == null) {
            throw failure(
                    "\"" + candidate.toString() + "\" expected.");
        }
        return lexeme;
    }

    /*--------------------------------------------------------------*/
    public static class Test {
        private static final Token COMMA = TokenType.COMMA.getToken();
        private static final Token IN = Token.create("'IN'");
        private static final Token INPUT = Token.create("INPUT");
        private static final Token IDENTIFIER = TokenType.IDENTIFIER.getToken();

        public static void main(String[] args) throws ParseFailure {
            assert COMMA instanceof SimpleToken : "Factory Failure 1";
            assert IN instanceof WordToken : "Factory Failure 2";
            assert INPUT instanceof WordToken : "Factory Failure 3";
            assert IDENTIFIER instanceof RegexToken : "Factory Failure 4";

            Scanner analyzer = new Scanner(",aBc In input inputted");

            assert analyzer.advance() == COMMA : "COMMA unrecognized";
            assert analyzer.advance() == IDENTIFIER : "ID unrecognized";
            assert analyzer.advance() == IN : "IN unrecognized";
            assert analyzer.advance() == INPUT : "INPUT unrecognized";
            assert analyzer.advance() == IDENTIFIER : "ID unrecognized 1";

            analyzer = new Scanner("Abc IN\nCde");
            analyzer.advance(); // advance to first token.

            assert (analyzer.matchAdvance(TokenType.IDENTIFIER).equals("Abc"));
            assert (analyzer.advance().toString().equals("in"));
            assert (analyzer.matchAdvance(TokenType.IDENTIFIER).equals("Cde"));

            // Deliberately force an exception toss
            analyzer = new Scanner("xyz\nabc + def");
            analyzer.advance();
            analyzer.advance();
            try {
                analyzer.advance(); // should throw an exception
                assert false : "Error Detection Failure";
            } catch (ParseFailure e) {
                assert e.getErrorReport().equals(
                        "Line 2:\n"
                                + "abc + def\n"
                                + "____^\n");
            }

            System.out.println("Scanner PASSED");

            System.exit(0);
        }
    }
}
