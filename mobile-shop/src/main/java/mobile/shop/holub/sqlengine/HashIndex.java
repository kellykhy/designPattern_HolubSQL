package mobile.shop.holub.sqlengine;

import java.util.HashMap;
import java.util.Map;
import mobile.shop.holub.datastorage.table.Table;

public class HashIndex {
    private final String indexedColumn;
    private final Map<String, Table> index;

    public HashIndex(String indexedColumn) {
        index = new HashMap<>();
        this.indexedColumn = indexedColumn;
    }


    public boolean isIndexedColumn(String columnName) {
        return indexedColumn.equals(columnName);
    }

    public void addSubTable(String value, Table table) {
        index.put(value, table);
    }

    public Table getSubTable(String value) {
        return index.get(value);
    }
}
