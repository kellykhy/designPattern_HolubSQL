package mobile.shop.holub.sqlengine.expressionvisitor;

import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.sqlengine.HashIndex;
import mobile.shop.holub.sqlengine.expression.ArithmeticExpression;
import mobile.shop.holub.sqlengine.expression.AtomicExpression;
import mobile.shop.holub.sqlengine.expression.LikeExpression;
import mobile.shop.holub.sqlengine.expression.LogicalExpression;
import mobile.shop.holub.sqlengine.expression.NotExpression;
import mobile.shop.holub.sqlengine.expression.RelationalExpression;

public class IndexCheckVisitor implements Visitor {
    private final HashIndex hashIndex;
    private Table subTable;
    private boolean isIndexed;

    public IndexCheckVisitor(HashIndex hashIndex) {
        this.hashIndex = hashIndex;
        subTable = null;
        isIndexed = false;
    }

    @Override
    public void visit(RelationalExpression relationalExpression) {
        if (relationalExpression.isEqualOperation()) {
            relationalExpression.visitLeftOperand(this);
        }
        if (isIndexed) {
            String value = relationalExpression.getRightValue();
            subTable = hashIndex.getSubTable(value);
        }
    }

    @Override
    public void visit(AtomicExpression atomicExpression) {
        String columnName = atomicExpression.getColumnName();
        if (columnName != null) {
            isIndexed = hashIndex.isIndexedColumn(columnName);
        }
    }

    @Override
    public void visit(LikeExpression likeExpression) {

    }

    @Override
    public void visit(ArithmeticExpression arithmeticExpression) {

    }

    @Override
    public void visit(LogicalExpression logicalExpression) {

    }

    @Override
    public void visit(NotExpression notExpression) {

    }

    public Table getSubTable() {
        return subTable;
    }
}
