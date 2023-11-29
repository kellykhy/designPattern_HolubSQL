package mobile.shop.holub.sqlengine.enums;

import mobile.shop.holub.sqlengine.text.Token;

public enum TokenType {

    COMMA(Token.create("',")),
    EQUAL(Token.create("'=")),
    LP(Token.create("'(")),
    RP(Token.create("')")),
    DOT(Token.create("'.")),
    STAR(Token.create("'*")),
    SLASH(Token.create("'/")),
    AND(Token.create("'AND")),
    BEGIN(Token.create("'BEGIN")),
    COMMIT(Token.create("'COMMIT")),
    CREATE(Token.create("'CREATE")),
    DATABASE(Token.create("'DATABASE")),
    INDEX(Token.create("'INDEX")),
    DELETE(Token.create("'DELETE")),
    DROP(Token.create("'DROP")),
    DUMP(Token.create("'DUMP")),
    FROM(Token.create("'FROM")),
    INSERT(Token.create("'INSERT")),
    INTO(Token.create("'INTO")),
    KEY(Token.create("'KEY")),
    LIKE(Token.create("'LIKE")),
    NOT(Token.create("'NOT")),
    NULL(Token.create("'NULL")),
    OR(Token.create("'OR")),
    PRIMARY(Token.create("'PRIMARY")),
    ROLLBACK(Token.create("'ROLLBACK")),
    SELECT(Token.create("'SELECT")),
    SET(Token.create("'SET")),
    TABLE(Token.create("'TABLE")),
    UPDATE(Token.create("'UPDATE")),
    USE(Token.create("'USE")),
    VALUES(Token.create("'VALUES")),
    WHERE(Token.create("'WHERE")),

    WORK(Token.create("WORK|TRAN(SACTION)?")),
    ADDITIVE(Token.create("\\+|-")),
    STRING(Token.create("(\".*?\")|('.*?')")),
    RELOP(Token.create("[<>][=>]?")),
    NUMBER(Token.create("[0-9]+(\\.[0-9]+)?")),

    INTEGER(Token.create("(small|tiny|big)?int(eger)?")),
    NUMERIC(Token.create("decimal|numeric|real|double")),
    CHAR(Token.create("(var)?char")),
    DATE(Token.create("date(\\s*\\(.*?\\))?")),

    IDENTIFIER(Token.create("[a-zA-Z_0-9/\\\\:~]+"));

    private final Token token;


    TokenType(Token token) {
        this.token = token;
    }


    public Token getToken() {
        return token;
    }

    public String toString() {
        return this.getToken().toString();
    }
}