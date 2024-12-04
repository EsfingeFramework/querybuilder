package polyglot.demo;

import dao.ProductDAO;
import ef.qb.core.QueryBuilder;
import org.junit.jupiter.api.Test;

public class DemoTest {

    @Test
    public void demoTest() {
        var productDAO = QueryBuilder.create(ProductDAO.class);
        var products = productDAO.getProduct();
        for (var product : products) {
            // print products with rating list
            System.out.println(product);
        }
    }

}
