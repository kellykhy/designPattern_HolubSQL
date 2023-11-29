package mobile.shop.holub.sqlengine.expressionvisitor;

import mobile.shop.holub.sqlengine.expression.ArithmeticExpression;
import mobile.shop.holub.sqlengine.expression.AtomicExpression;
import mobile.shop.holub.sqlengine.expression.LikeExpression;
import mobile.shop.holub.sqlengine.expression.LogicalExpression;
import mobile.shop.holub.sqlengine.expression.NotExpression;
import mobile.shop.holub.sqlengine.expression.RelationalExpression;

public interface Visitor {
    public void visit(ArithmeticExpression arithmeticExpression);

    public void visit(AtomicExpression atomicExpression);

    public void visit(LikeExpression likeExpression);

    public void visit(LogicalExpression logicalExpression);

    public void visit(NotExpression notExpression);

    public void visit(RelationalExpression relationalExpression);

}