package polyglot.demo;

import dao.CustomerDAO;
import ef.qb.core.QueryBuilder;
import org.junit.jupiter.api.Test;

public class DemoTest {

    @Test
    public void demoTest() {
        var customerDAO = QueryBuilder.create(CustomerDAO.class);
        var customers = customerDAO.getCustomer();
        for (var customer : customers) {
            // print customer with transitions
            System.out.println(customer);
        }
    }

}
