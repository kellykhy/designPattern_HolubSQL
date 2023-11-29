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
package mobile.shop.holub.sqlengine;


import static mobile.shop.holub.sqlengine.enums.MathOperator.DIVIDE;
import static mobile.shop.holub.sqlengine.enums.MathOperator.MINUS;
import static mobile.shop.holub.sqlengine.enums.MathOperator.PLUS;
import static mobile.shop.holub.sqlengine.enums.MathOperator.TIMES;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.EQ;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.GE;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.GT;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.LE;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.LT;
import static mobile.shop.holub.sqlengine.enums.RelationalOperator.NE;
import static mobile.shop.holub.sqlengine.enums.TokenType.ADDITIVE;
import static mobile.shop.holub.sqlengine.enums.TokenType.AND;
import static mobile.shop.holub.sqlengine.enums.TokenType.BEGIN;
import static mobile.shop.holub.sqlengine.enums.TokenType.CHAR;
import static mobile.shop.holub.sqlengine.enums.TokenType.COMMA;
import static mobile.shop.holub.sqlengine.enums.TokenType.COMMIT;
import static mobile.shop.holub.sqlengine.enums.TokenType.CREATE;
import static mobile.shop.holub.sqlengine.enums.TokenType.DATABASE;
import static mobile.shop.holub.sqlengine.enums.TokenType.DATE;
import static mobile.shop.holub.sqlengine.enums.TokenType.DELETE;
import static mobile.shop.holub.sqlengine.enums.TokenType.DOT;
import static mobile.shop.holub.sqlengine.enums.TokenType.DROP;
import static mobile.shop.holub.sqlengine.enums.TokenType.DUMP;
import static mobile.shop.holub.sqlengine.enums.TokenType.EQUAL;
import static mobile.shop.holub.sqlengine.enums.TokenType.FROM;
import static mobile.shop.holub.sqlengine.enums.TokenType.IDENTIFIER;
import static mobile.shop.holub.sqlengine.enums.TokenType.INDEX;
import static mobile.shop.holub.sqlengine.enums.TokenType.INSERT;
import static mobile.shop.holub.sqlengine.enums.TokenType.INTEGER;
import static mobile.shop.holub.sqlengine.enums.TokenType.INTO;
import static mobile.shop.holub.sqlengine.enums.TokenType.KEY;
import static mobile.shop.holub.sqlengine.enums.TokenType.LIKE;
import static mobile.shop.holub.sqlengine.enums.TokenType.LP;
import static mobile.shop.holub.sqlengine.enums.TokenType.NOT;
import static mobile.shop.holub.sqlengine.enums.TokenType.NULL;
import static mobile.shop.holub.sqlengine.enums.TokenType.NUMBER;
import static mobile.shop.holub.sqlengine.enums.TokenType.NUMERIC;
import static mobile.shop.holub.sqlengine.enums.TokenType.OR;
import static mobile.shop.holub.sqlengine.enums.TokenType.PRIMARY;
import static mobile.shop.holub.sqlengine.enums.TokenType.RELOP;
import static mobile.shop.holub.sqlengine.enums.TokenType.ROLLBACK;
import static mobile.shop.holub.sqlengine.enums.TokenType.RP;
import static mobile.shop.holub.sqlengine.enums.TokenType.SELECT;
import static mobile.shop.holub.sqlengine.enums.TokenType.SET;
import static mobile.shop.holub.sqlengine.enums.TokenType.SLASH;
import static mobile.shop.holub.sqlengine.enums.TokenType.STAR;
import static mobile.shop.holub.sqlengine.enums.TokenType.STRING;
import static mobile.shop.holub.sqlengine.enums.TokenType.TABLE;
import static mobile.shop.holub.sqlengine.enums.TokenType.UPDATE;
import static mobile.shop.holub.sqlengine.enums.TokenType.USE;
import static mobile.shop.holub.sqlengine.enums.TokenType.VALUES;
import static mobile.shop.holub.sqlengine.enums.TokenType.WHERE;
import static mobile.shop.holub.sqlengine.enums.TokenType.WORK;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.datastorage.Selector;
import mobile.shop.holub.datastorage.exporter.CSVExporter;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.datastorage.table.TableFactory;
import mobile.shop.holub.datastorage.table.UnmodifiableTable;
import mobile.shop.holub.sqlengine.enums.MathOperator;
import mobile.shop.holub.sqlengine.enums.RelationalOperator;
import mobile.shop.holub.sqlengine.expression.Expression;
import mobile.shop.holub.sqlengine.expression.ExpressionFactory;
import mobile.shop.holub.sqlengine.expressionvisitor.IndexCheckVisitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.text.Scanner;
import mobile.shop.holub.sqlengine.value.Value;
import mobile.shop.holub.sqlengine.value.ValueFactory;
import mobile.shop.holub.tools.FilePath;
import mobile.shop.holub.tools.ThrowableContainer;


public final class Database {
    private final Map<String, HashIndex> indices = new HashMap<>();

    private final Map tables = new TableMap(new HashMap());
    private File location = new File(FilePath.resourceFilePath);

    private int affectedRows = 0;

    private int transactionLevel = 0;
    private String expression;    // SQL expression being parsed
    private Scanner in;            // The current scanner.

    public Database() {
        System.out.println(location.getAbsoluteFile());
    }

    public Database(URI directory) throws IOException {
        useDatabase(new File(directory));
    }

    public Database(File path) throws IOException {
        useDatabase(path);
    }

    //@declarations-end
    //--------------------------------------------------------------

    /**
     * Use the indicated directory for the database
     */
    public Database(String path) throws IOException {
        useDatabase(new File(path));
    }


    public Database(File path, Table[] database) throws IOException {
        useDatabase(path);
        for (int i = 0; i < database.length; ++i) {
            tables.put(database[i].name(), database[i]);
        }
    }


    private void error(String message) throws ParseFailure {
        throw in.failure(message);
    }

    /**
     * Like {@link #error}, but throws the exception only if the test fails.
     */
    private void verify(boolean test, String message) throws ParseFailure {
        if (!test) {
            throw in.failure(message);
        }
    }

    /**
     * Use an existing "database." In the current implementation, a "database" is a directory and tables are files
     * within the directory. An active database (opened by a constructor, a USE DATABASE directive, or a prior call to
     * the current method) is closed and committed before the new database is opened.
     *
     * @param path A {@link File} object that specifies directory that represents the database.
     * @throws IOException if the directory that represents the database can't be found.
     */
    public void useDatabase(File path) throws IOException {
        dump();
        tables.clear();    // close old database if there is one
        this.location = path;
    }

    //--------------------------------------------------------------
    // Private parse-related workhorse functions.

    /**
     * Create a database by opening the indicated directory. All tables must be files in that directory. If you don't
     * call this method (or issue a SQL CREATE DATABASE directive), then the current directory is used.
     *
     * @throws IOException if the named directory can't be opened.
     */

    // 디렉터리 패스 변경하는 부분 제거
    public void createDatabase(String name) throws IOException {
//        File location = new File(name);
//        location.mkdir();
//        this.location = location;
    }

    /**
     * Create a new table. If a table by this name exists, it's overwritten.
     */
    public void createTable(String name, List columns) {
        String[] columnNames = new String[columns.size()];
        int i = 0;
        for (Iterator names = columns.iterator(); names.hasNext(); ) {
            columnNames[i++] = (String) names.next();
        }

        Table newTable = TableFactory.create(name, columnNames);
        tables.put(name, newTable);
    }

    //--------------------------------------------------------------
    // Public methods that duplicate some SQL statements.
    // The SQL interpreter calls these methods to
    // do the actual work.

    /**
     * Destroy both internal and external (on the disk) versions of the specified table.
     */
    public void dropTable(String name) {
        tables.remove(name);    // ignore the error if there is one.

        File tableFile = new File(location, name);
        if (tableFile.exists()) {
            tableFile.delete();
        }
    }

    /**
     * Flush to the persistent store (e.g. disk) all tables that are "dirty" (which have been modified since the
     * database was last committed). These tables will not be flushed again unless they are modified after the current
     * dump() call. Nothing happens if no tables are dirty.
     * <p>
     * The present implemenation flushes to a .csv file whose name is the table name with a ".csv" extension added.
     */
    public void dump() throws IOException {
        Collection values = tables.values();
        if (values != null) {
            for (Iterator i = values.iterator(); i.hasNext(); ) {
                Table current = (Table) i.next();
                if (current.isDirty()) {
                    System.out.println("-----------------------------------------");
                    System.out.println(location);
                    System.out.println("-----------------------------------------");
                    Writer out =
                            new FileWriter(
                                    new File(location, current.name() + ".csv"));
                    current.export(new CSVExporter(out));
                    out.close();
                }
            }
        }
    }


    public int affectedRows() {
        return affectedRows;
    }

    /**
     * Begin a transaction
     */
    public void begin() {
        ++transactionLevel;

        Collection currentTables = tables.values();
        for (Iterator i = currentTables.iterator(); i.hasNext(); ) {
            ((Table) i.next()).begin();
        }
    }

    /**
     * Commit transactions at the current level.
     *
     * @throws NoSuchElementException if no <code>begin()</code> was issued.
     */
    public void commit() throws ParseFailure {
        assert transactionLevel > 0 : "No begin() for commit()";
        --transactionLevel;

        try {
            Collection currentTables = tables.values();
            for (Iterator i = currentTables.iterator(); i.hasNext(); ) {
                ((Table) i.next()).commit(Table.THIS_LEVEL);
            }
        } catch (NoSuchElementException e) {
            verify(false, "No BEGIN to match COMMIT");
        }
    }

    /**
     * Roll back transactions at the current level
     *
     * @throws NoSuchElementException if no <code>begin()</code> was issued.
     */
    public void rollback() throws ParseFailure {
        assert transactionLevel > 0 : "No begin() for commit()";
        --transactionLevel;
        try {
            Collection currentTables = tables.values();

            for (Iterator i = currentTables.iterator(); i.hasNext(); ) {
                ((Table) i.next()).rollback(Table.THIS_LEVEL);
            }
        } catch (NoSuchElementException e) {
            verify(false, "No BEGIN to match ROLLBACK");
        }
    }
    //@transactions-start
    //----------------------------------------------------------------------
    // Transaction processing.


    public Table execute(String expression) throws IOException, ParseFailure {
        try {
            this.expression = expression;
            in = new Scanner(expression);
            in.advance();    // advance to the first token.
            return statement();
        } catch (ParseFailure e) {
            if (transactionLevel > 0) {
                rollback();
            }
            throw e;
        } catch (IOException e) {
            if (transactionLevel > 0) {
                rollback();
            }
            throw e;
        }
    }


    private Table statement() throws ParseFailure, IOException {
        affectedRows = 0;    // is modified by UPDATE, INSERT, DELETE

        // These productions map to public method calls:
        if (in.matchAdvance(CREATE) != null) {

            if (in.match(DATABASE)) {
                in.advance();
                createDatabase(in.required(IDENTIFIER));
            } else if (in.match(INDEX)) {
                in.advance();
                String tableName = in.required(IDENTIFIER);
                in.advance();
                String column = in.required(IDENTIFIER);
                createIndex(tableName, column);
            } else {
                in.required(TABLE);
                String tableName = in.required(IDENTIFIER);
                in.required(LP);
                createTable(tableName, declarations());
                in.required(RP);
            }
        } else if (in.matchAdvance(DROP) != null) {
            in.required(TABLE);
            dropTable(in.required(IDENTIFIER));
        } else if (in.matchAdvance(USE) != null) {
            in.required(DATABASE);
            useDatabase(new File(in.required(IDENTIFIER)));
        } else if (in.matchAdvance(BEGIN) != null) {
            in.matchAdvance(WORK);    // ignore it if it's there
            begin();
        } else if (in.matchAdvance(ROLLBACK) != null) {
            in.matchAdvance(WORK);    // ignore it if it's there
            rollback();
        } else if (in.matchAdvance(COMMIT) != null) {
            in.matchAdvance(WORK);    // ignore it if it's there
            commit();
        } else if (in.matchAdvance(DUMP) != null) {
            dump();
        }

        // These productions must be handled via an
        // interpreter:

        else if (in.matchAdvance(INSERT) != null) {
            in.required(INTO);
            String tableName = in.required(IDENTIFIER);

            List columns = null, values = null;

            if (in.matchAdvance(LP) != null) {
                columns = idList();
                in.required(RP);
            }
            if (in.required(VALUES) != null) {
                in.required(LP);
                values = exprList();
                in.required(RP);
            }
            affectedRows = doInsert(tableName, columns, values);
        } else if (in.matchAdvance(UPDATE) != null) {    // First parse the expression
            String tableName = in.required(IDENTIFIER);
            in.required(SET);
            final String columnName = in.required(IDENTIFIER);
            in.required(EQUAL);
            final Expression value = expr();
            in.required(WHERE);
            affectedRows =
                    doUpdate(tableName, columnName, value, expr());
        } else if (in.matchAdvance(DELETE) != null) {
            in.required(FROM);
            String tableName = in.required(IDENTIFIER);
            in.required(WHERE);
            affectedRows = doDelete(tableName, expr());
        } else if (in.matchAdvance(SELECT) != null) {
            List columns = idList();

            String into = null;
            if (in.matchAdvance(INTO) != null) {
                into = in.required(IDENTIFIER);
            }

            in.required(FROM);
            List requestedTableNames = idList();

            Expression where = (in.matchAdvance(WHERE) == null)
                    ? null : expr();

//            Visitor visitor = new PrintVisitor();
//            if (where != null) {
//                where.accept(visitor);
//                System.out.println();
//            }

            Table result = applyIndex(requestedTableNames, where);

            if (result != null) {
                return result;
            }
            return doSelect(columns, into, requestedTableNames, where);

        } else {
            error("Expected insert, create, drop, use, "
                    + "update, delete or select");
        }

        return null;
    }

    private List idList() throws ParseFailure {
        List identifiers = null;

        if (in.matchAdvance(STAR) == null) {
            identifiers = new ArrayList();
            String id;
            while ((id = in.required(IDENTIFIER)) != null) {
                identifiers.add(id);
                if (in.matchAdvance(COMMA) == null) {
                    break;
                }
            }
        }

        return identifiers;
    }


    private List declarations() throws ParseFailure {
        List identifiers = new ArrayList();

        String id;
        while (true) {
            if (in.matchAdvance(PRIMARY) != null) {
                in.required(KEY);
                in.required(LP);
                in.required(IDENTIFIER);
                in.required(RP);
            } else {
                id = in.required(IDENTIFIER);

                identifiers.add(id);    // get the identifier

                // Skip past a type declaration if one's there

                if ((in.matchAdvance(INTEGER) != null)
                        || (in.matchAdvance(CHAR) != null)) {
                    if (in.matchAdvance(LP) != null) {
                        expr();
                        in.required(RP);
                    }
                } else if (in.matchAdvance(NUMERIC) != null) {
                    if (in.matchAdvance(LP) != null) {
                        expr();
                        in.required(COMMA);
                        expr();
                        in.required(RP);
                    }
                } else if (in.matchAdvance(DATE) != null) {
                    ; // do nothing
                }

                in.matchAdvance(NOT);
                in.matchAdvance(NULL);
            }

            if (in.matchAdvance(COMMA) == null) // no more columns
            {
                break;
            }
        }

        return identifiers;
    }

    private List exprList() throws ParseFailure {
        List expressions = new LinkedList();

        expressions.add(expr());
        while (in.matchAdvance(COMMA) != null) {
            expressions.add(expr());
        }
        return expressions;
    }
    //----------------------------------------------------------------------
    // idList			::= IDENTIFIER idList' | STAR
    // idList'			::= COMMA IDENTIFIER idList'
    // 					|	e
    // Return a Collection holding the list of columns
    // or null if a * was found.

    /**
     * Top-level expression production. Returns an Expression object which will interpret the expression at runtime when
     * you call it's evaluate() method.
     * <PRE>
     * expr    ::=     andExpr expr' expr'   ::= OR  andExpr expr' |   e
     * </PRE>
     */

    private Expression expr() throws ParseFailure {

        Expression left = andExpr();
        while (in.matchAdvance(OR) != null) {
            left = ExpressionFactory.getLogicalExpression(left, OR, andExpr());
        }
        return left;
    }

    //----------------------------------------------------------------------
    // declarations  ::= IDENTIFIER [type] declaration'
    // declarations' ::= COMMA IDENTIFIER [type] [NOT [NULL]] declarations'
    //				 |   e
    //
    // type			 ::= INTEGER [ LP expr RP 				]
    //				 |	 CHAR 	 [ LP expr RP				]
    //				 |	 NUMERIC [ LP expr COMMA expr RP	]
    //				 |	 DATE			// format spec is part of token

    private Expression andExpr() throws ParseFailure {
        Expression left = relationalExpr();
        while (in.matchAdvance(AND) != null) {
            left = ExpressionFactory.getLogicalExpression(left, AND, relationalExpr());
        }
        return left;
    }

    // exprList 		::= 	  expr exprList'
    // exprList'		::= COMMA expr exprList'
    // 					|	e

    private Expression relationalExpr() throws ParseFailure {
        Expression left = additiveExpr();
        while (true) {
            String lexeme;
            if ((lexeme = in.matchAdvance(RELOP)) != null) {
                RelationalOperator op;
                if (lexeme.length() == 1) {
                    op = lexeme.charAt(0) == '<' ? LT : GT;
                } else {
                    if (lexeme.charAt(0) == '<' && lexeme.charAt(1) == '>') {
                        op = NE;
                    } else {
                        op = lexeme.charAt(0) == '<' ? LE : GE;
                    }
                }

                left = ExpressionFactory.getRelationalExpression(left, op, additiveExpr());
            } else if (in.matchAdvance(EQUAL) != null) {
                left = ExpressionFactory.getRelationalExpression(left, EQ, additiveExpr());
            } else if (in.matchAdvance(LIKE) != null) {

                left = ExpressionFactory.getLikeExpression(left, additiveExpr());
            } else {
                break;
            }
        }
        return left;
    }

    private Expression additiveExpr() throws ParseFailure {
        String lexeme;
        Expression left = multiplicativeExpr();
        while ((lexeme = in.matchAdvance(ADDITIVE)) != null) {

            MathOperator op = lexeme.charAt(0) == '+' ? PLUS : MINUS;
            left = ExpressionFactory.getArithmeticExpression(left, multiplicativeExpr(), op);
        }
        return left;
    }

    // andExpr			::= 	relationalExpr andExpr'
    // andExpr'			::= AND relationalExpr andExpr'
    // 					|	e

    private Expression multiplicativeExpr() throws ParseFailure {
        Expression left = term();
        while (true) {
            if (in.matchAdvance(STAR) != null) {
                left = ExpressionFactory.getArithmeticExpression(left, term(), TIMES);
                ;
            } else if (in.matchAdvance(SLASH) != null) {
                left = ExpressionFactory.getArithmeticExpression(left, term(), DIVIDE);
            } else {
                break;
            }
        }
        return left;
    }

    // relationalExpr ::=   		additiveExpr relationalExpr'
    // relationalExpr'::=	  RELOP additiveExpr relationalExpr'
    // 						| EQUAL additiveExpr relationalExpr'
    // 						| LIKE  additiveExpr relationalExpr'
    // 						| e

    private Expression term() throws ParseFailure {
        if (in.matchAdvance(NOT) != null) {
            return ExpressionFactory.getNotExpression(expr());
        } else if (in.matchAdvance(LP) != null) {
            Expression toReturn = expr();
            in.required(RP);
            return toReturn;
        } else {
            return factor();
        }
    }

    // additiveExpr	::= 			 multiplicativeExpr additiveExpr'
    // additiveExpr'	::= ADDITIVE multiplicativeExpr additiveExpr'
    // 					|	e

    private Expression factor() throws ParseFailure {
        try {
            String lexeme;
            Value result;

            if ((lexeme = in.matchAdvance(STRING)) != null) {
                result = ValueFactory.getStringValue(lexeme);

            } else if ((lexeme = in.matchAdvance(NUMBER)) != null) {
                result = ValueFactory.getNumericValue(lexeme);

            } else if ((lexeme = in.matchAdvance(NULL)) != null) {

                result = ValueFactory.getNullValue();
            } else {
                String columnName = in.required(IDENTIFIER);
                String tableName = null;

                if (in.matchAdvance(DOT) != null) {
                    tableName = columnName;
                    columnName = in.required(IDENTIFIER);
                }

                result = ValueFactory.getIdValue(tableName, columnName);
            }
            return ExpressionFactory.getAtomicExpression(result, tables);
        } catch (java.text.ParseException e) { /* fall through */ }

        error("Couldn't parse Number"); // Always throws a ParseFailure
        return null;
    }

    // multiplicativeExpr	::=       term multiplicativeExpr'
    // multiplicativeExpr'	::= STAR  term multiplicativeExpr'
    // 						|	SLASH term multiplicativeExpr'
    // 						|	e

    //@value-end
    //@workhorse-start
    //======================================================================
    // Workhorse methods called from the parser.
    //
    private Table doSelect(List columns, String into,
                           List requestedTableNames,
                           final Expression where)
            throws ParseFailure {

        Iterator tableNames = requestedTableNames.iterator();

        assert tableNames.hasNext() : "No tables to use in select!";

        if (columns == null) {
            columns = new ArrayList<String>();
            for (Object requestedTableName : requestedTableNames) {
                String tableName = (String) requestedTableName;
                Table table = (Table) tables.get(tableName);
                Cursor cursor = table.rows();

                for (int i = 0; i < cursor.columnCount(); i++) {
                    columns.add(cursor.columnName(i));
                }

            }
        }

        // The primary table is the first one listed in the
        // FROM clause. The participantsInJoin are the other
        // tables listed in the FROM clause. We're passed in the
        // table names; use these names to get the actual Table
        // objects.

        Table primary = (Table) tables.get((String) tableNames.next());

        List participantsInJoin = new ArrayList();
        while (tableNames.hasNext()) {
            String participant = (String) tableNames.next();
            participantsInJoin.add(tables.get(participant));
        }

        // Now do the select operation. First create a Strategy
        // object that picks the correct rows, then pass that
        // object through to the primary table's select() method.

        Selector selector = (where == null) ? Selector.ALL : //{=Database.selector}
                new Selector.Adapter() {
                    public boolean approve(Cursor[] tables) {
                        try {

                            Value result = where.evaluate(tables);

                            return result.getBooleanValue();
                        } catch (ParseFailure e) {
                            throw new ThrowableContainer(e);
                        } catch (UnsupportedOperationException e) {
                            String errorMessage = e.getMessage();
                            throw new ThrowableContainer(in.failure(errorMessage));
                        }

                    }
                };

        try {
            Table result = primary.select(selector, columns, participantsInJoin);

            // If this is a "SELECT INTO <table>" request, remove the
            // returned table from the UnmodifiableTable wrapper, give
            // it a name, and put it into the tables Map.

            if (into != null) {
                result = ((UnmodifiableTable) result).extract();
                result.rename(into);
                tables.put(into, result);
            }
            return result;
        } catch (ThrowableContainer container) {
            throw (ParseFailure) container.contents();
        }
    }

    // term				::=	NOT expr
    // 					|	LP expr RP
    // 					|	factor

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private int doInsert(String tableName, List columns, List values)
            throws ParseFailure {
        List processedValues = new LinkedList();
        Table t = (Table) tables.get(tableName);

        for (Iterator i = values.iterator(); i.hasNext(); ) {
            Expression current = (Expression) i.next();
            processedValues.add(
                    current.evaluate(null).toString());
        }

        // finally, put the values into the table.

        if (columns == null) {
            return t.insert(processedValues);
        }

        verify(columns.size() == values.size(),
                "There must be a value for every listed column");
        return t.insert(columns, processedValues);
    }

    // factor			::= compoundId | STRING | NUMBER | NULL
    // compoundId		::= IDENTIFIER compoundId'
    // compoundId'		::= DOT IDENTIFIER
    // 					|	e

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private int doUpdate(String tableName, final String columnName,
                         final Expression value, final Expression where) throws ParseFailure {
        Table t = (Table) tables.get(tableName);
        try {
            return t.update
                    (new Selector() {
                         public boolean approve(Cursor[] tables) {
                             try {
                                 Value result = where.evaluate(tables);

                                 return result.getBooleanValue();
                             } catch (ParseFailure e) {
                                 throw new ThrowableContainer(e);
                             } catch (UnsupportedOperationException e) {
                                 String errorMessage = e.getMessage();
                                 throw new ThrowableContainer(in.failure(errorMessage));
                             }

                         }

                         public void modify(Cursor current) {
                             try {
                                 Value newValue = value.evaluate(new Cursor[]{current});
                                 current.update(columnName, newValue.toString());
                             } catch (ParseFailure e) {
                                 throw new ThrowableContainer(e);
                             }
                         }
                     }
                    );
        } catch (ThrowableContainer container) {
            throw (ParseFailure) container.contents();
        }
    }

    private int doDelete(String tableName, final Expression where) throws ParseFailure {
        Table t = (Table) tables.get(tableName);
        try {
            return t.delete
                    (new Selector.Adapter() {
                         public boolean approve(Cursor[] tables) {
                             try {
                                 Value result = where.evaluate(tables);

                                 return result.getBooleanValue();
                             } catch (ParseFailure e) {
                                 throw new ThrowableContainer(e);
                             } catch (UnsupportedOperationException e) {
                                 String errorMessage = e.getMessage();
                                 throw new ThrowableContainer(in.failure(errorMessage));
                             }
                         }
                     }
                    );
        } catch (ThrowableContainer container) {
            throw (ParseFailure) container.contents();
        }
    }


    private Table applyIndex(List requestedTableNames, Expression where) {
        if (requestedTableNames.size() == 1 && where != null) {

            String tableName = (String) requestedTableNames.get(0);
            if (indices.containsKey(tableName)) {

                HashIndex hashIndex = indices.get(tableName);
                IndexCheckVisitor visitor = new IndexCheckVisitor(hashIndex);
                where.accept(visitor);
                return visitor.getSubTable();
            }
        }
        return null;
    }


    private void createIndex(String tableName, String columnName)
            throws IOException, ParseFailure {

        Table indexedTable = (Table) tables.get(tableName);
        Cursor cursor = indexedTable.rows();

        HashIndex hashIndex = new HashIndex(columnName);
        Set<String> indexedValues = new HashSet<>();
        while (cursor.advance()) {
            String value = cursor.column(columnName).toString();

            if (!indexedValues.contains(value)) {
                String query = "select * from "
                        + tableName + " where "
                        + columnName + " = " + value;
                Table subTable = execute(query);
                hashIndex.addSubTable(value, subTable);
                indexedValues.add(value);
            }
        }
        indices.put(tableName, hashIndex);
    }


    /**
     * A Map proxy that hanldes lazy instatiation of tables from the disk.
     */
    private final class TableMap implements Map {
        private final Map realMap;

        public TableMap(Map realMap) {
            this.realMap = realMap;
        }

        /**
         * If the requested table is already in memory, return it. Otherwise load it from the disk.
         */
        public Object get(Object key) {
            String tableName = (String) key;
            try {
                Table desiredTable = (Table) realMap.get(tableName);
                if (desiredTable == null) {
                    desiredTable = TableFactory.load(
                            tableName + ".csv", location);
                    put(tableName, desiredTable);
                }
                return desiredTable;
            } catch (IOException e) {    // Can't use verify(...) or error(...) here because the
                // base-class "get" method doesn't throw any exceptions.
                // Kludge a runtime-exception toss. Call in.failure()
                // to get an exception object that calls out the
                // input file name and line number, then transmogrify
                // the ParseFailure to a RuntimeException.

                String message =
                        "Table not created internally and couldn't be loaded."
                                + "(" + e.getMessage() + ")\n";
                throw new RuntimeException(
                        in.failure(message).getMessage());
            }
        }

        public Object put(Object key, Object value) {    // If transactions are active, put the new
            // table into the same transaction state
            // as the other tables.

            for (int i = 0; i < transactionLevel; ++i) {
                ((Table) value).begin();
            }

            return realMap.put(key, value);
        }

        public void putAll(Map m) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return realMap.size();
        }

        public boolean isEmpty() {
            return realMap.isEmpty();
        }

        public Object remove(Object k) {
            return realMap.remove(k);
        }

        public void clear() {
            realMap.clear();
        }

        public Set keySet() {
            return realMap.keySet();
        }

        public Collection values() {
            return realMap.values();
        }

        public Set entrySet() {
            return realMap.entrySet();
        }

        public boolean equals(Object o) {
            return realMap.equals(o);
        }

        public int hashCode() {
            return realMap.hashCode();
        }

        public boolean containsKey(Object k) {
            return realMap.containsKey(k);
        }

        public boolean containsValue(Object v) {
            return realMap.containsValue(v);
        }
    }
}