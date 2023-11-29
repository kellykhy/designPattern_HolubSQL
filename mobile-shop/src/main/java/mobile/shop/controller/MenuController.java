package mobile.shop.controller;

import java.io.IOException;
import mobile.shop.holub.sqlengine.Database;
import mobile.shop.holub.sqlengine.text.ParseFailure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    Database database = new Database();

    @GetMapping("/manus")
    public String getMenus(@RequestParam String restaurantId) throws IOException, ParseFailure {
        String query = "SELECT * FROM menu WHERE restaurant_id = " + restaurantId;
        
        return "";
    }

}
