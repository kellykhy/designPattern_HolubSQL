package mobile.shop.holub;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import mobile.shop.holub.datastorage.importer.CSVImporter;
import mobile.shop.holub.datastorage.table.ConcreteTable;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.sqlengine.Database;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.tools.FilePath;
import org.junit.jupiter.api.Test;

public class DatabaseTest {
    @Test
    void test() throws IOException, ParseFailure {
        Database theDatabase = new Database();

        BufferedReader sql = new BufferedReader(
                new FileReader(FilePath.resourceFilePath + "Database.test.sql"));

        String test;
        while ((test = sql.readLine()) != null) {
            test = test.trim();
            if (test.length() == 0) {
                continue;
            }

            while (test.endsWith("\\")) {
                test = test.substring(0, test.length() - 1);
                test += sql.readLine().trim();
            }

            System.out.println("Parsing: " + test);
            Table result = theDatabase.execute(test);

            if (result != null)    // it was a SELECT of some sort
            {
                System.out.println(result.toString());
            }
        }

        try {
            theDatabase.execute("insert garbage SQL");
            System.out.println("Database FAILED");
            System.exit(1);
        } catch (ParseFailure e) {
            System.out.println("Correctly found garbage SQL:\n"
                    + e + "\n"
                    + e.getErrorReport());
        }

        theDatabase.dump();

        System.out.println("Database PASSED");
        System.exit(0);
    }


    @Test
    void testToJson() throws IOException {
        Reader in = new FileReader(FilePath.resourceFilePath + "menu.csv");
        Table.Importer importer = new CSVImporter(in);

        Table table = new ConcreteTable(importer);

        System.out.println(table.toJson());
    }
}