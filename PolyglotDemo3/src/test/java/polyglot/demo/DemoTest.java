package polyglot.demo;

import dao.RatingDAO;
import ef.qb.core.QueryBuilder;
import org.junit.jupiter.api.Test;

public class DemoTest {

    @Test
    public void demoTest() {
        var ratingDAO = QueryBuilder.create(RatingDAO.class);
        var ratings = ratingDAO.getRatingMax();
        for (var rating : ratings) {
            // print max rating with items
            System.out.println(rating);
        }
    }

}
