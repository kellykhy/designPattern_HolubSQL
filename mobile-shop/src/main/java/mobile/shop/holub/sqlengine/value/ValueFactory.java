package mobile.shop.holub.sqlengine.value;

import java.text.ParseException;

public class ValueFactory {
    public static Value getBooleanValue(boolean value) {
        return new BooleanValue(value);
    }

    public static Value getIdValue(String tableName, String columnName) {
        return new IdValue(tableName, columnName);
    }

    public static Value getNullValue() {
        return new NullValue();
    }

    public static Value getNumericValue(double value) {
        return new NumericValue(value);
    }

    public static Value getNumericValue(String value) throws ParseException {
        return new NumericValue(value);
    }

    public static Value getStringValue(String lexeme) {
        return new StringValue(lexeme);
    }
}
