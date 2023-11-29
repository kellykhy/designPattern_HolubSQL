package mobile.shop.holub.sqlengine.expression;


import static mobile.shop.holub.sqlengine.enums.MathOperator.*;

import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.enums.MathOperator;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.NumericValue;
import mobile.shop.holub.sqlengine.value.Value;

public class ArithmeticExpression extends Expression {

    private static final String NON_NUMERIC_EXCEPTION_MESSAGE = "Operands to < > <= >= = must be Numbers";
    private final MathOperator operator;
    private final Expression left, right;

    public ArithmeticExpression(Expression left, Expression right, MathOperator operator) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public Value evaluate(Cursor[] tables) throws ParseFailure {
        Value leftValue = left.evaluate(tables);
        Value rightValue = right.evaluate(tables);

        try {
            double l = leftValue.getNumericValue();
            double r = rightValue.getNumericValue();

            return new NumericValue
                    ((operator == PLUS) ? (l + r) :
                            (operator == MINUS) ? (l - r) :
                                    (operator == TIMES) ? (l * r) :
                                            /* operator == DIVIDE  */   (l / r)
                    );
        } catch (UnsupportedOperationException unsupportedOperationException) {
            throw new UnsupportedOperationException(NON_NUMERIC_EXCEPTION_MESSAGE);
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
}