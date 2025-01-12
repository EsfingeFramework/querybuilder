package polyglot.demo;

import dao.ProductDAO;
import ef.qb.core.QueryBuilder;
import java.util.ArrayList;
import java.util.Random;
import model.Product;
import model.Rating;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class DemoTest {

    ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        productDAO = QueryBuilder.create(ProductDAO.class);
    }

    @Test
    @Order(1)
    public void listTest() {
        var products = productDAO.list();
        assertNotNull(products, "The list should not be null");
        assertFalse(products.isEmpty(), "The list should not be empty");
    }

    @Test
    @Order(2)
    public void createTest() {
        var p = new Product();
        p.setCategory("createtest_product_category");
        p.setDescription("createtest_product createtest_product createtest_product createtest_product");
        p.setName("createtest_product_name");
        p.setPage("http://createtest_product_page");
        p.setPrice(99.d);
        p.setSku(new Random().nextInt() + "");

        var ratings = new ArrayList<Rating>();
        var r1 = new Rating();
        r1.setComment("createtest_rating_comment");
        r1.setSource("createtest_rating_source");
        r1.setValue(5);
        ratings.add(r1);
        p.setRatings(ratings);
        var savedProduct = productDAO.save(p);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getRatings().get(0).getId());
    }

    @Test
    @Order(3)
    public void updateTest() {
        var products = productDAO.getProductByName("createtest_product_name");
        for (var p : products) {
            p.setName("updatetest_product_name");
            var updatedProduct = productDAO.save(p);
            assertNotNull(updatedProduct);
            Assertions.assertEquals("updatetest_product_name", updatedProduct.getName());
        }
    }

    @Test
    @Order(4)
    public void deleteTest() {
        var products = productDAO.getProductByName("updatetest_product_name");
        for (var p : products) {
            assertDoesNotThrow(() -> {
                productDAO.delete(p);
            });
        }
    }
}
