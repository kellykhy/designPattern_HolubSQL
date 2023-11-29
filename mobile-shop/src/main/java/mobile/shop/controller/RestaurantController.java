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
    Database database = new Database();

    @GetMapping("/restaurants")
    public String getRestaurant() throws IOException, ParseFailure {

        BufferedReader sql = new BufferedReader(
                new FileReader(FilePath.resourceFilePath + "createQuery.sql"));

        String test;

        String jsonString = null;
        while ((test = sql.readLine()) != null) {
            test = test.trim();
            if (test.isEmpty()) {
                continue;
            }

            while (test.endsWith("\\")) {
                test = test.substring(0, test.length() - 1);
                test += sql.readLine().trim();
            }

            System.out.println("Parsing: " + test);
            Table result = database.execute(test);

            if (result != null)    // it was a SELECT of some sort
            {
                jsonString = result.toJson();
            }
        }
        database.dump();

        return jsonString;

    }
}
