package mobile.shop.holub.sqlengine.expressionvisitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import mobile.shop.holub.sqlengine.Database;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.tools.FilePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrintVisitorTest {
    Database database;

    @BeforeEach
    public void setUp() throws IOException {
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
    void testPrintVisitorOutput() throws IOException, ParseFailure {

        database.execute("SELECT * FROM menu WHERE id = 5");
        database.execute("SELECT * FROM menu WHERE id <> 5");
        database.execute("SELECT * FROM menu WHERE id < 5");
        database.execute("SELECT * FROM menu WHERE id > 5");
        database.execute("SELECT * FROM menu WHERE id <= 5");
        database.execute("SELECT * FROM menu WHERE id >= 5");

        database.execute("SELECT * FROM menu WHERE id = 5 AND name = 'Pizza'");
        database.execute("SELECT * FROM menu WHERE id = 5 OR price <= 10");
        database.execute("SELECT * FROM menu WHERE NOT (id = 5 AND type = 'Beverage')");
        database.execute("SELECT * FROM menu WHERE name LIKE 'Cheese%'");
    }
}