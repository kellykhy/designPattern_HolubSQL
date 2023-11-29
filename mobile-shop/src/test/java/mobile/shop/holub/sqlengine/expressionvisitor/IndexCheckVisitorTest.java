package mobile.shop.holub.sqlengine.expressionvisitor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.datastorage.table.TableFactory;
import mobile.shop.holub.sqlengine.HashIndex;
import mobile.shop.holub.sqlengine.enums.RelationalOperator;
import mobile.shop.holub.sqlengine.expression.AtomicExpression;
import mobile.shop.holub.sqlengine.expression.Expression;
import mobile.shop.holub.sqlengine.expression.RelationalExpression;
import mobile.shop.holub.sqlengine.value.IdValue;
import mobile.shop.holub.sqlengine.value.NumericValue;
import mobile.shop.holub.sqlengine.value.StringValue;
import mobile.shop.holub.sqlengine.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexCheckVisitorTest {
    String tableName = "dummyTable";
    String columnName = "dummyColumn";
    HashIndex hashIndex;
    Value valiLeftValue;
    Value validRightValue;
    int dummyKey;

    @BeforeEach
    void init() {
        dummyKey = 1;
        hashIndex = new HashIndex(columnName);
        Table dummySubTable = TableFactory.create(tableName, new String[]{columnName});

        hashIndex.addSubTable(String.valueOf(dummyKey), dummySubTable);

        valiLeftValue = new IdValue(tableName, columnName);

        validRightValue = new NumericValue(dummyKey);
    }

    @Test
    void testIndexUsualCase() {
        Expression where = getWhere(valiLeftValue, validRightValue, RelationalOperator.EQ);
        IndexCheckVisitor indexCheckVisitor = new IndexCheckVisitor(hashIndex);
        where.accept(indexCheckVisitor);

        Table subTable = indexCheckVisitor.getSubTable();
        assertNotNull(subTable);
    }


    @Test
    void testIndexUnusualCase() {
        Expression where = getWhere(valiLeftValue, validRightValue, RelationalOperator.NE);
        IndexCheckVisitor indexCheckVisitor = new IndexCheckVisitor(hashIndex);
        where.accept(indexCheckVisitor);
        Table subTable = indexCheckVisitor.getSubTable();
        assertNull(subTable);

        ;
        where = getWhere(new StringValue("invalid value"),
                validRightValue, RelationalOperator.EQ);
        where.accept(indexCheckVisitor);
        subTable = indexCheckVisitor.getSubTable();
        assertNull(subTable);

        where = getWhere(valiLeftValue, new StringValue("invalid value"),
                RelationalOperator.EQ);
        where.accept(indexCheckVisitor);
        subTable = indexCheckVisitor.getSubTable();
        assertNull(subTable);
    }


    private Expression getWhere(Value leftValue, Value rightValue, RelationalOperator relop) {
        Expression left = new AtomicExpression(leftValue, new HashMap());
        Expression right = new AtomicExpression(rightValue, new HashMap());
        return new RelationalExpression(left, relop, right);
    }
}