package mobile.shop.holub.database.datastoreage.importer;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import mobile.shop.holub.datastorage.importer.CSVImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVImporterTest {

    CSVImporter importer;

    @BeforeEach
    void init() throws IOException {
        Reader in = new FileReader("people");
        importer = new CSVImporter(in);
        importer.startTable();
    }

    @Test
    void testLoadTableName() throws IOException {
        String tableNme = importer.loadTableName();
        assertEquals("people", tableNme);
    }

    @Test
    void testLoadColumns() throws IOException {
        Iterator columnNames = importer.loadColumnNames();
        String last = (String) columnNames.next();
        assertEquals("last", last);
        String first = (String) columnNames.next();
        assertEquals("first", first);
        String addrId = (String) columnNames.next();
        assertEquals("addrId", addrId);
    }

    @Test
    void testLoadRows() throws IOException {
        Iterator row = importer.loadRow();
        String last = (String) row.next();
        assertEquals("Holub", last);
        String first = (String) row.next();
        assertEquals("Allen", first);
        String addrId = (String) row.next();
        assertEquals("1", addrId);
    }


}