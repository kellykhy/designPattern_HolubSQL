package mobile.shop.holub.sqlengine.expression;

import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.enums.TokenType;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.BooleanValue;
import mobile.shop.holub.sqlengine.value.Value;

import static mobile.shop.holub.sqlengine.enums.TokenType.*;

public class LogicalExpression extends Expression {

    private final boolean isAnd;
    private final Expression left, right;

    public LogicalExpression(Expression left, TokenType op, Expression right) {
        assert op == AND || op == OR;
        this.isAnd = (op == AND);
        this.left = left;
        this.right = right;
    }

    public Value evaluate(Cursor[] tables) throws ParseFailure {
        Value leftValue = left.evaluate(tables);
        Value rightValue = right.evaluate(tables);

        boolean l = leftValue.getBooleanValue();
        boolean r = rightValue.getBooleanValue();

        return new BooleanValue(isAnd ? (l && r) : (l || r));
    }


    public void printVisit(Visitor visitor) {
        System.out.print("(");
        left.accept(visitor);
        String operator = " OR ";
        if (isAnd) {
            operator = " AND ";
        }
        System.out.print(operator);
        right.accept(visitor);
        System.out.print(")");
    }

    @Override
    public void accept(Visitor visitor) {

        visitor.visit(this);

    }
}
