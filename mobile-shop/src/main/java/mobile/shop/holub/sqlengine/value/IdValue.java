package mobile.shop.holub.sqlengine.value;

import java.util.Map;
import mobile.shop.holub.datastorage.Cursor;
import mobile.shop.holub.datastorage.table.Table;

public class IdValue extends Value {
    String tableName;
    String columnName;

    public IdValue(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    /**
     * Using the cursor, extract the referenced cell from the current Row and return it's contents as a String.
     *
     * @return the value as a String or null if the cell was null.
     */
    public String toString(Cursor[] participants, Map tables) {
        Object content = null;

        // If no name is to the left of the dot, then use
        // the (only) table.

        if (tableName == null) {
            content = participants[0].column(columnName);
        } else {
            Table container = (Table) tables.get(tableName);

            // Search for the table whose name matches
            // the one to the left of the dot, then extract
            // the desired column from that table.

            content = null;
            for (int i = 0; i < participants.length; ++i) {
                if (participants[i].isTraversing(container)) {
                    content = participants[i].column(columnName);
                    break;
                }
            }
        }

        // All table contents are converted to Strings, whatever
        // their original type. This conversion can cause
        // problems if the table was created manually.
//        System.out.println("-------------");
//        System.out.println(participants[0].tableName());
//        System.out.println(columnName);
//        System.out.println("-------------");
        return (content == null) ? null : content.toString();
    }

    public Value value(Cursor[] participants, Map tables) {

        String s = toString(participants, tables);
        try {
            return (s == null)
                    ? (Value) new NullValue()
                    : (Value) new NumericValue(s)
                    ;
        } catch (java.text.ParseException e) {    // The NumericValue constructor failed, so it must be
            // a string. Fall through to the return-a-string case.
        }
        return new StringValue(s);
    }

    @Override
    public String toString() {
        if (tableName == null) {
            return columnName;
        }
        return tableName + "::" + columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}