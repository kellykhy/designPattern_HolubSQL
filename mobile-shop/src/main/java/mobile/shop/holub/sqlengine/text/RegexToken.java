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

/**
 * Matches a token specified by a regular expression. (as described in the {@link Pattern} class.
 *
 * @include /etc/license.txt
 * @see Pattern
 * @see Token
 */

public class RegexToken extends Token {
    private final Pattern pattern;
    private final String id;
    private Matcher matcher;


    public RegexToken(String description) {
        id = description;
        pattern = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
    }

    public boolean match(String input, int offset) {
        matcher = pattern.matcher(input.substring(offset));
        return matcher.lookingAt();
    }

    public String lexeme() {
        return matcher.group();
    }

    public String toString() {
        return id;
    }
}
