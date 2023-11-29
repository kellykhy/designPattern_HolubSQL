package mobile.shop.holub.sqlengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.tools.FilePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseTest {
    Database database;

    @BeforeEach
    void init() throws IOException, ParseFailure {

        database = new Database();
        BufferedReader sql = new BufferedReader(
                new FileReader(FilePath.resourceFilePath + "createQuery.sql"));
        String test;
        while ((test = sql.readLine()) != null) {
            test = test.trim();
            if (test.isEmpty()) {
                continue;
            }

            while (test.endsWith("\\")) {
                test = test.substring(0, test.length() - 1);
                test += sql.readLine().trim();
            }
        }

    }

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
    void testIndex() throws IOException, ParseFailure {
        String tableName = "menu";
        String columnName = "restaurant_id";

        int iterationCount = 1000;
        String query = "SELECT *" + " FROM " + tableName + " WHERE " + columnName + " = 2";
        Table t1 = null;
        Table t2 = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterationCount; i++) {
            t1 = database.execute(query);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("execution time without index: " + elapsedTime + "ms");

        // after index
        database.execute("CREATE INDEX menu ON restaurant_id");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterationCount; i++) {
            t2 = database.execute(query);
        }

        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("execution time with index: " + elapsedTime + "ms");

        assertEquals(t1, t2);
    }


}