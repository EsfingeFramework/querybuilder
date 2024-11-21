package controller;

import dao.ProductDAO;
import ef.qb.core.QueryBuilder;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    public static void main(String args[]) {
        var result = new HashMap<String, Integer>();
        var productDAO = QueryBuilder.create(ProductDAO.class);
        var products = productDAO.getProductBySegment("Frozen Foods");
        for (var product : products) {
            for (var interaction : product.getInteractions()) {
                if (interaction.getType().equals("add_to_cart")) {
                    var key = product.getName();
                    result.put(key, result.getOrDefault(key, 0) + 1);
                }
            }
        }
        var maxEntry = result.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        if (maxEntry != null) {
            System.out.println(maxEntry.getKey() + ": " + maxEntry.getValue());
        }
    }
}
