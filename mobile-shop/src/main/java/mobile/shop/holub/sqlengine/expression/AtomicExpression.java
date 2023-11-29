package mobile.shop.holub.sqlengine.expression;

import java.util.Map;
import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.value.IdValue;
import mobile.shop.holub.sqlengine.value.Value;

public class AtomicExpression extends Expression {

    private final Value atom;
    private Map tables;

    public AtomicExpression(Value atom, Map tables) {

        this.tables = tables;
        this.atom = atom;
    }

    public Value evaluate(Cursor[] tableList) {
        return atom instanceof IdValue
                ? ((IdValue) atom).value(tableList, tables)    // lookup cell in table and
                : atom                            // convert to appropriate type
                ;
    }


    public void printVisit() {
        System.out.print(atom.toString());
    }

    @Override
    public String toString() {
        return atom.toString();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getColumnName() {
        if (atom instanceof IdValue) {
            return ((IdValue) atom).getColumnName();
        }
        return null;
    }
}
