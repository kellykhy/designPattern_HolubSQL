package mobile.shop.holub.sqlengine.expression;


import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.BooleanValue;
import mobile.shop.holub.sqlengine.value.Value;

public class LikeExpression extends Expression {

    private static final String NON_STRING_EXCEPTION_MESSAGE = "Both operands to LIKE must be strings";

    private final Expression left, right;

    public LikeExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Value evaluate(Cursor[] tables) throws ParseFailure {
        Value leftValue = left.evaluate(tables);
        Value rightValue = right.evaluate(tables);

        try {
            String compareTo = leftValue.getStringValue();
            String regex = rightValue.getStringValue();
            regex = regex.replaceAll("%", ".*");
            return new BooleanValue(compareTo.matches(regex));
        } catch (UnsupportedOperationException unsupportedOperationException) {
            throw new UnsupportedOperationException(NON_STRING_EXCEPTION_MESSAGE);
        }


    }

    public void printVisit(Visitor visitor) {
        System.out.print("(");
        left.accept(visitor);
        System.out.print(" LIKE ");
        right.accept(visitor);
        System.out.print(")");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
