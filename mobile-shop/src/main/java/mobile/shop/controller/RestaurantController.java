package mobile.shop.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.sqlengine.Database;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import mobile.shop.holub.tools.FilePath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RestaurantController {
    Database database;

    public RestaurantController() throws IOException, ParseFailure {
        database = initDatabase();
    }

    @GetMapping("/restaurants")
    public String getRestaurant() throws IOException, ParseFailure {
        String query = "SELECT * FROM restaurant";
        Table table = database.execute(query);

        return table.toJson();
    }

    private Database initDatabase() throws IOException, ParseFailure {
        Database database = new Database();

        BufferedReader sql = new BufferedReader(
                new FileReader(FilePath.resourceFilePath + "createQuery.sql"));

        String line;
        while ((line = sql.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            while (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1);
                line += sql.readLine().trim();
            }

        }
        database.execute("CREATE INDEX menu ON restaurant_id");
        return database;
    }
}
