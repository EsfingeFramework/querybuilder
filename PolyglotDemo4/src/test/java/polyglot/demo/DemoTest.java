package polyglot.demo;

import dao.CustomerDAO;
import ef.qb.core.QueryBuilder;
import java.util.ArrayList;
import java.util.Date;
import model.Address;
import model.Customer;
import model.Stage;
import model.Start;
import model.Transition;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoTest {

    CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        customerDAO = QueryBuilder.create(CustomerDAO.class);
    }

    @Test
    @Order(1)
    public void listTest() {
        var customers = customerDAO.list();
        assertNotNull(customers, "The list should not be null");
        assertFalse(customers.isEmpty(), "The list should not be empty");
    }

    @Test
    @Order(2)
    public void createTest() {

        var customer = new Customer();
        customer.setEmail("createtest_customer_email");
        customer.setLogin("createtest_customer_login");
        customer.setName("createtest_customer_name");
        customer.setPhone("(99) 99999-9999");
        var address = new Address();
        address.setCity("createtest_address_city");
        address.setComplement("createtest_address_complement");
        address.setFederalunit("createtest_address_federalunit");
        address.setNumber(Long.MIN_VALUE);
        address.setStreet("createtest_address_street");
        address.setZipcode("createtest_address_zipcode");
        customer.setAddress(address);
        var savedCustomer = customerDAO.save(customer);

        var transitions = new ArrayList<Transition>();
        var transition = new Transition();
        transition.setTimestamp(new Date());
        var start = new Start();
        start.setSession("createtest_start_session");
        start.setCustomer(savedCustomer.getLogin());
        transition.setCustomer(start);
        var stage = new Stage();
        stage.setLocation("createtest_stage_location");
        stage.setUrl("createtest_stage_url");
        transition.setPage(stage);
        transitions.add(transition);

        savedCustomer.setTransitions(transitions);
        savedCustomer = customerDAO.save(savedCustomer);

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getTransitions().get(0).getId());
    }

    @Test
    @Order(3)
    public void updateTest() {
        var customers = customerDAO.getCustomerByEmail("createtest_customer_email");
        for (var customer : customers) {
            customer.setEmail("updatetest_customer_email");
            var updatedCustomer = customerDAO.save(customer);
            assertNotNull(updatedCustomer);
            assertEquals("updatetest_customer_email", updatedCustomer.getEmail());
        }
    }

    @Test
    @Order(4)
    public void deleteTest() {
        var ratings = customerDAO.getCustomerByEmail("updatetest_customer_email");
        for (var rating : ratings) {
            assertDoesNotThrow(() -> {
                customerDAO.delete(rating);
            });
        }
    }

}
