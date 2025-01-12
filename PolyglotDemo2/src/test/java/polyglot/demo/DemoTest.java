package polyglot.demo;

import dao.CartDAO;
import ef.qb.core.QueryBuilder;
import java.util.Date;
import model.Address;
import model.Cart;
import model.Customer;
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

    CartDAO cartDAO;

    @BeforeEach
    void setUp() {
        cartDAO = QueryBuilder.create(CartDAO.class);
    }

    @Test
    @Order(1)
    public void listTest() {
        var carts = cartDAO.list();
        assertNotNull(carts, "The list should not be null");
        assertFalse(carts.isEmpty(), "The list should not be empty");
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

        var cart = new Cart();
        cart.setStatus("createtest_cart_status");
        cart.setTimestamp(new Date());
        cart.setCustomer("createtest_customer_login");
        cart.setShopper(customer);

        var savedCart = cartDAO.save(cart);
        assertNotNull(savedCart);
        assertNotNull(savedCart.getShopper().getId());
    }

    @Test
    @Order(3)
    public void polyglotQueryTest() {
        var carts = cartDAO.getCartByShopperEmail("createtest_customer_email");
        for (var cart : carts) {
            System.out.println(cart);
        }
        assertNotNull(carts, "The list should not be null");
        assertFalse(carts.isEmpty(), "The list should not be empty");
    }

    @Test
    @Order(4)
    public void updateTest() {
        var carts = cartDAO.getCartByStatus("createtest_cart_status");
        for (var cart : carts) {
            cart.setStatus("updatetest_cart_status");
            var updatedCart = cartDAO.save(cart);
            assertNotNull(updatedCart);
            assertEquals("updatetest_cart_status", updatedCart.getStatus());
        }
    }

    @Test
    @Order(5)
    public void deleteTest() {
        var carts = cartDAO.getCartByStatus("updatetest_cart_status");
        for (var cart : carts) {
            assertDoesNotThrow(() -> {
                cartDAO.delete(cart);
            });
        }
    }
}
