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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *  An input token (a lexical symbol in the input stream).
 *    @include /etc/license.txt
 */

public abstract class Token {
    /**
     * Returns true if the sequence at the indicated offset from the beginning of the indicated input string matches the
     * current token.
     */
    private static final Pattern metacharacters =
            Pattern.compile("[\\\\\\[\\]{}$\\^*+?|()]");

    private static boolean containsRegexMetacharacters(
            String s) {    // This method could be implemented more efficiently,
        // but its not called very often.
        Matcher m = metacharacters.matcher(s);
        return m.find();
    }

    public static Token create(String spec) {
        Token token;
        int start = 1;

        if (!spec.startsWith("'")) {
            if (containsRegexMetacharacters(spec)) {
                token = new RegexToken(spec);
                return token;
            }

            --start;    // don't compensate for leading quote

            // fall through to the "quoted-spec" case
        }

        int end = spec.length();

        if (start == 1 && spec.endsWith("'")) // saw leading '
        {
            --end;
        }

        token = Character.isJavaIdentifierPart(spec.charAt(end - 1))
                ? (Token) new WordToken(spec.substring(start, end))
                : (Token) new SimpleToken(spec.substring(start, end))
        ;

        return token;
    }


    abstract boolean match(String input, int offset);

    /**
     * Returns the input string that matched the token specification.
     */
    abstract String lexeme();
}
