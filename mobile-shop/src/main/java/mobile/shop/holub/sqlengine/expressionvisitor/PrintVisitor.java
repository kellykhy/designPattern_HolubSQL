package mobile.shop.holub.sqlengine.expressionvisitor;


import mobile.shop.holub.sqlengine.expression.ArithmeticExpression;
import mobile.shop.holub.sqlengine.expression.AtomicExpression;
import mobile.shop.holub.sqlengine.expression.LikeExpression;
import mobile.shop.holub.sqlengine.expression.LogicalExpression;
import mobile.shop.holub.sqlengine.expression.NotExpression;
import mobile.shop.holub.sqlengine.expression.RelationalExpression;

public class PrintVisitor implements Visitor {

    public void visit(ArithmeticExpression arithmeticExpression) {
        arithmeticExpression.printVisit(this);
    }

    public void visit(AtomicExpression atomicExpression) {
        atomicExpression.printVisit();
    }

    public void visit(LikeExpression likeExpression) {
        likeExpression.printVisit(this);
    }

    public void visit(LogicalExpression logicalExpression) {
        logicalExpression.printVisit(this);
    }

    public void visit(NotExpression notExpression) {
        notExpression.printVisit(this);
    }

    public void visit(RelationalExpression relationalExpression) {
        relationalExpression.printVisit(this);
    }
}



