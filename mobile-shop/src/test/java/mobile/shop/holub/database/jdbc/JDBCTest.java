package mobile.shop.holub.database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import mobile.shop.holub.tools.FilePath;
import org.junit.jupiter.api.Test;

class JDBCTest {

    static String[] data =
            {"(1,  'John',   'Mon', 1, 'JustJoe')",
                    "(2,  'JS',     'Mon', 1, 'Cappuccino')",
                    "(3,  'Marie',  'Mon', 2, 'CaffeMocha')",
            };


    @Test
    void test() throws Exception {
        Class.forName("mobile.shop.holub.jdbc.JDBCDriver") //{=JDBCTest.forName}
                .newInstance();

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(            //{=JDBCTest.getConnection}

                    FilePath.dbPath,
                    "harpo", "swordfish");
            statement = connection.createStatement();

            statement.executeUpdate(
                    "create table test (" +
                            "  Entry      INTEGER      NOT NULL" +
                            ", Customer   VARCHAR (20) NOT NULL" +
                            ", DOW        VARCHAR (3)  NOT NULL" +
                            ", Cups       INTEGER      NOT NULL" +
                            ", Type       VARCHAR (10) NOT NULL" +
                            ", PRIMARY KEY( Entry )" +
                            ")"
            );

            for (int i = 0; i < data.length; ++i) {
                statement.executeUpdate(
                        "insert into test VALUES " + data[i]);
            }

            // Test Autocommit stuff. If everything's working
            // correctly, there James should be in the databse,
            // but Fred should not.

            connection.setAutoCommit(false);
            statement.executeUpdate(
                    "insert into test VALUES " +
                            "(4, 'James',  'Thu', 1, 'Cappuccino')");
            connection.commit();

            statement.executeUpdate(
                    "insert into test (Customer) VALUES('Fred')");
            connection.rollback();
            connection.setAutoCommit(true);

            // Print everything.

            ResultSet result = statement.executeQuery("select * from test");
            while (result.next()) {
                System.out.println
                        (result.getInt("Entry") + ", "
                                + result.getString("Customer") + ", "
                                + result.getString("DOW") + ", "
                                + result.getInt("Cups") + ", "
                                + result.getString("Type")
                        );
            }
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }
    }
}