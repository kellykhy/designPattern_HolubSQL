package mobile.shop.holub.sqlengine.expressionvisitor;

import mobile.shop.holub.sqlengine.expression.ArithmeticExpression;
import mobile.shop.holub.sqlengine.expression.AtomicExpression;
import mobile.shop.holub.sqlengine.expression.LikeExpression;
import mobile.shop.holub.sqlengine.expression.LogicalExpression;
import mobile.shop.holub.sqlengine.expression.NotExpression;
import mobile.shop.holub.sqlengine.expression.RelationalExpression;

public class QueryOptimizationVisitor implements Visitor {
    @Override
    public void visit(ArithmeticExpression arithmeticExpression) {

    }

    @Override
    public void visit(AtomicExpression atomicExpression) {

    }

    @Override
    public void visit(LikeExpression likeExpression) {

    }

    @Override
    public void visit(LogicalExpression logicalExpression) {

    }

    @Override
    public void visit(NotExpression notExpression) {

    }

    @Override
    public void visit(RelationalExpression relationalExpression) {

    }
}
