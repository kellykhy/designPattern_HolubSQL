package mobile.shop.holub.sqlengine.expression;

import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.BooleanValue;
import mobile.shop.holub.sqlengine.value.Value;

public class NotExpression extends Expression {

    private static final String NON_PROPER_OPERAND_EXCEPTION_MESSAGE = "operands to NOT must be logical/relational";
    private final Expression operand;

    public NotExpression(Expression operand) {
        this.operand = operand;
    }

    public Value evaluate(Cursor[] tables) throws ParseFailure {
        Value value = operand.evaluate(tables);
        try {
            return new BooleanValue(!(value.getBooleanValue()));
        } catch (UnsupportedOperationException unsupportedOperationException) {
            throw new UnsupportedOperationException(NON_PROPER_OPERAND_EXCEPTION_MESSAGE);
        }

    }

    public void printVisit(Visitor visitor) {
        System.out.print("(");
        System.out.print("NOT ");
        operand.accept(visitor);
        System.out.print(")");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
