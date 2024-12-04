package polyglot.demo;

import dao.CartDAO;
import ef.qb.core.QueryBuilder;
import org.junit.jupiter.api.Test;

public class DemoTest {

    @Test
    public void demoTest() {
        var cartDAO = QueryBuilder.create(CartDAO.class);
        var carts = cartDAO.getCartActive();
        for (var cart : carts) {
            // print active carts with shoppers
            System.out.println(cart);
        }
    }

}
