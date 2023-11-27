package mobile.shop.holub.sqlengine.expression;

import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.sqlengine.expressionvisitor.Visitor;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.sqlengine.value.Value;

public abstract class Expression {


    abstract public Value evaluate(Cursor[] tables) throws ParseFailure;

    abstract public void accept(Visitor visitor);

}
