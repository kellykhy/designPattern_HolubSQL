package mobile.shop.holub.sqlengine.expression;

import java.util.Map;
import mobile.shop.holub.sqlengine.enums.MathOperator;
import mobile.shop.holub.sqlengine.enums.RelationalOperator;
import mobile.shop.holub.sqlengine.enums.TokenType;
import mobile.shop.holub.sqlengine.value.Value;

public class ExpressionFactory {

    public static Expression getArithmeticExpression(Expression left, Expression right,
                                                     MathOperator operator) {
        return new ArithmeticExpression(left, right, operator);
    }

    public static Expression getAtomicExpression(Value atom, Map tables) {
        return new AtomicExpression(atom, tables);
    }

    public static Expression getLikeExpression(Expression left, Expression right) {
        return new LikeExpression(left, right);
    }

    public static Expression getLogicalExpression(Expression left, TokenType op, Expression right) {
        return new LogicalExpression(left, op, right);
    }

    public static Expression getNotExpression(Expression operand) {
        return new NotExpression(operand);
    }

    public static Expression getRelationalExpression(Expression left, RelationalOperator operator,
                                                     Expression right) {
        return new RelationalExpression(left, operator, right);
    }
}
