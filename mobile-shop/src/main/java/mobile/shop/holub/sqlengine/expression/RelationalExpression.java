package mobile.shop.holub.sqlengine.expression;

import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.enums.RelationalOperator;
import mobile.shop.holub.sqlengine.expressionvisitor.IndexCheckVisitor;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.BooleanValue;
import mobile.shop.holub.sqlengine.value.NullValue;
import mobile.shop.holub.sqlengine.value.NumericValue;
import mobile.shop.holub.sqlengine.value.StringValue;
import mobile.shop.holub.sqlengine.value.Value;

import static mobile.shop.holub.sqlengine.enums.RelationalOperator.*;

public class RelationalExpression extends Expression {

    private static final String NON_NUMERIC_OPERAND_EXCEPTION_MESSAGE = "Operands must be numbers";
    private final RelationalOperator operator;
    private final Expression left, right;

    public RelationalExpression(Expression left, RelationalOperator operator, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public Value evaluate(Cursor[] tables) throws ParseFailure {
        Value leftValue = left.evaluate(tables);
        Value rightValue = right.evaluate(tables);

        if (operator == EQ || operator == NE) {
            if ((leftValue instanceof StringValue)
                    || (rightValue instanceof StringValue)) {

                boolean isEqual =
                        leftValue.toString().equals(rightValue.toString());

                return new BooleanValue((operator == EQ) == isEqual);
            }

            if (rightValue instanceof NullValue
                    || leftValue instanceof NullValue) {

                boolean isEqual =
                        leftValue.getClass() == rightValue.getClass();

                return new BooleanValue((operator == EQ) == isEqual);
            }
        }

        // Convert Boolean values to numbers so we can compare them.
        //
        if (leftValue instanceof BooleanValue) {
            leftValue = new NumericValue(
                    leftValue.getBooleanValue() ? 1 : 0);
        }
        if (rightValue instanceof BooleanValue) {
            rightValue = new NumericValue(
                    rightValue.getBooleanValue() ? 1 : 0);
        }

        try {
            double l = leftValue.getNumericValue();
            double r = rightValue.getNumericValue();
            return new BooleanValue
                    ((operator == EQ) ? (l == r) :
                            (operator == NE) ? (l != r) :
                                    (operator == LT) ? (l > r) :
                                            (operator == GT) ? (l < r) :
                                                    (operator == LE) ? (l <= r) :
                                                            /* operator == GE	 */   (l >= r)
                    );
        } catch (UnsupportedOperationException unsupportedOperationException) {
            throw new UnsupportedOperationException(NON_NUMERIC_OPERAND_EXCEPTION_MESSAGE);
        }


    }


    public void printVisit(Visitor visitor) {
        System.out.print("(");
        left.accept(visitor);
        System.out.print(" " + operator.toString() + " ");
        right.accept(visitor);
        System.out.print(")");

    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

    }

    public boolean isEqualOperation() {
        return this.operator == EQ;
    }

    public void visitLeftOperand(Visitor visitor) {
        left.accept(visitor);
    }

    public String getRightValue() {
        return right.toString();
    }
}
