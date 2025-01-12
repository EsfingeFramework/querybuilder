package polyglot.demo;

import dao.RatingDAO;
import ef.qb.core.QueryBuilder;
import java.util.ArrayList;
import java.util.UUID;
import model.Item;
import model.Rating;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class DemoTest {

    RatingDAO ratingDAO;

    @BeforeEach
    void setUp() {
        ratingDAO = QueryBuilder.create(RatingDAO.class);
    }

    @Test
    @Order(1)
    public void listTest() {
        var ratings = ratingDAO.list();
        assertNotNull(ratings, "The list should not be null");
        assertFalse(ratings.isEmpty(), "The list should not be empty");
    }

    @Test
    @Order(2)
    public void domainTermsTest() {
        var ratings = ratingDAO.getRatingMax();
        assertNotNull(ratings, "The list should not be null");
        assertFalse(ratings.isEmpty(), "The list should not be empty");
    }

    @Test
    @Order(3)
    public void createTest() {
        var rating = new Rating();
        rating.setComment("createtest_rating_comment");
        rating.setProduct("createtest_rating_product");
        rating.setSource("createtest_rating_source");
        rating.setValue(5);

        var items = new ArrayList<Item>();
        var item1 = new Item();
        item1.setCart(UUID.fromString("75c5f207-5b95-4a83-9e1a-d5bbe78a92ec"));
        item1.setPrice(Double.MAX_VALUE);
        item1.setProduct("createtest_rating_product");
        item1.setQuantity(1);
        var item2 = new Item();
        item2.setCart(UUID.fromString("21a9e1b6-914d-47de-a77d-244e29257588"));
        item2.setPrice(Double.MAX_VALUE);
        item2.setProduct("createtest_rating_product");
        item2.setQuantity(3);
        items.add(item1);
        items.add(item2);
        rating.setItems(items);

        var savedRating = ratingDAO.save(rating);
        assertNotNull(savedRating);
        assertNotNull(savedRating.getItems().get(0).getId());
    }

    @Test
    @Order(4)
    public void updateTest() {
        var ratings = ratingDAO.getRatingBySource("createtest_rating_source");
        for (var rating : ratings) {
            rating.setSource("updatetest_rating_source");
            var updatedRating = ratingDAO.save(rating);
            assertNotNull(updatedRating);
            assertEquals("updatetest_rating_source", updatedRating.getSource());
        }
    }

    @Test
    @Order(5)
    public void deleteTest() {
        var ratings = ratingDAO.getRatingBySource("updatetest_rating_source");
        for (var rating : ratings) {
            assertDoesNotThrow(() -> {
                ratingDAO.delete(rating);
            });
        }
    }
}
