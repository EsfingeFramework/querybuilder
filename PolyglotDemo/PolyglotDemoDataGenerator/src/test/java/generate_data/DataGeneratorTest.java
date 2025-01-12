package generate_data;

import com.github.javafaker.Faker;
import dao.AddressDAO;
import dao.CartDAO;
import dao.CustomerDAO;
import dao.ItemDAO;
import dao.ProductDAO;
import dao.RatingDAO;
import dao.StageDAO;
import dao.StartDAO;
import dao.TransitionDAO;
import ef.qb.core.QueryBuilder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import model.Address;
import model.Cart;
import model.Customer;
import model.Item;
import model.Product;
import model.Rating;
import model.Stage;
import model.Start;
import model.Transition;
import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
public class DataGeneratorTest {

    private final AddressDAO addressDAO = QueryBuilder.create(AddressDAO.class);
    private final CartDAO cartDAO = QueryBuilder.create(CartDAO.class);
    private final CustomerDAO customerDAO = QueryBuilder.create(CustomerDAO.class);
    private final ItemDAO itemDAO = QueryBuilder.create(ItemDAO.class);
    private final ProductDAO productDAO = QueryBuilder.create(ProductDAO.class);
    private final RatingDAO ratingDAO = QueryBuilder.create(RatingDAO.class);
    private final StageDAO stageDAO = QueryBuilder.create(StageDAO.class);
    private final StartDAO startDAO = QueryBuilder.create(StartDAO.class);
    private final TransitionDAO transitionDAO = QueryBuilder.create(TransitionDAO.class);

    @Test
    public void init() {
        //populate();

        var sb = new StringBuilder("");
        sb.append(addressDAO.getAddress().size()).append(" addresses recorded.\n");
        sb.append(cartDAO.getCart().size()).append(" carts recorded.\n");
        sb.append(customerDAO.getCustomer().size()).append(" customers recorded.\n");
        sb.append(itemDAO.getItem().size()).append(" items recorded.\n");
        sb.append(productDAO.getProduct().size()).append(" products recorded.\n");
        sb.append(ratingDAO.getRating().size()).append(" rating recorded.\n");
        sb.append(stageDAO.getStage().size()).append(" stages recorded.\n");
        sb.append(startDAO.getStart().size()).append(" starts recorded.\n");
        sb.append(transitionDAO.getTransition().size()).append(" transitions recorded.\n");
        System.out.println(sb.toString());
    }

    private void populate() {
        var logins = populateCustomers();
        var products = populateProducts();
        populateCarts(logins, products);
        populateRatings(products);
        populateTransitions(logins, products);
    }

    private List<String> populateCustomers() {
        var faker = new Faker(new Locale("en-US"));
        var addresses = new ArrayList<Address>();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            var address = new Address();
            address.setCity(faker.address().city());
            address.setStreet(faker.address().streetName());
            address.setNumber((long) faker.number().numberBetween(1, 1000));
            address.setComplement("Apt " + faker.number().numberBetween(1, 100));
            address.setFederalunit(faker.address().stateAbbr());
            address.setZipcode(faker.address().zipCode());
            addresses.add(addressDAO.save(address));
        }
        var result = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            Customer customer = new Customer();
            var login = String.format("customer%03d", i);
            result.add(login);
            customer.setLogin(login);
            customer.setName(faker.name().fullName());
            customer.setEmail(faker.internet().emailAddress());
            customer.setPhone(faker.phoneNumber().cellPhone());
            customer.setAddress(addresses.get(random.nextInt(addresses.size())));
            customerDAO.save(customer);
        }
        return result;
    }

    private List<Product> populateProducts() {
        var faker = new Faker(new Locale("en-US"));
        var result = new ArrayList<Product>();
        for (int i = 0; i < 50; i++) {
            var product = new Product();
            product.setSku(String.valueOf(faker.number().randomNumber(10, true)));
            product.setName(faker.commerce().productName());
            product.setDescription(faker.lorem().sentence());
            String priceString = faker.commerce().price();
            Double price = Double.valueOf(priceString.replace(',', '.'));
            product.setPrice(price);
            product.setCategory(faker.commerce().department());
            product.setPage(faker.internet().url());
            result.add(productDAO.save(product));
        }
        return result;
    }

    private List<Cart> populateCarts(List<String> customers, List<Product> products) {
        var random = new Random();
        var result = new ArrayList<Cart>();
        Instant now = Instant.now();
        Instant oneYearAgo = now.minusSeconds(365L * 24 * 60 * 60);
        for (int i = 0; i < 200; i++) {
            var cart = new Cart();
            cart.setCustomer(customers.get(random.nextInt(customers.size())));
            cart.setStatus(weightedRandomStatus());
            long randomTimestamp = oneYearAgo.getEpochSecond()
                    + (long) (random.nextDouble() * (now.getEpochSecond() - oneYearAgo.getEpochSecond()));
            cart.setTimestamp(Timestamp.from(Instant.ofEpochSecond(randomTimestamp)));
            cart = cartDAO.save(cart);
            result.add(cart);

            int numberOfItems = random.nextInt(7) + 1;
            for (int j = 0; j < numberOfItems; j++) {
                var item = new Item();
                Product product = products.get(random.nextInt(products.size()));
                item.setCart(cart.getId());
                item.setProduct(product.getSku());
                int quantity = random.nextInt(5) + 1;
                double price = product.getPrice() * quantity;
                item.setQuantity(quantity);
                item.setPrice(price);
                itemDAO.save(item);
            }
        }
        return result;
    }

    private String weightedRandomStatus() {
        String[] statuses = {"active", "abandoned", "purchased"};
        int[] weights = {1, 1, 3};
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }
        int randomIndex = -1;
        double randomValue = Math.random() * totalWeight;
        for (int i = 0; i < weights.length; ++i) {
            randomValue -= weights[i];
            if (randomValue <= 0.0) {
                randomIndex = i;
                break;
            }
        }
        return statuses[randomIndex];
    }

    private void populateRatings(List<Product> products) {
        var faker = new Faker(new Locale("en-US"));
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            var rating = new Rating();
            rating.setProduct(products.get(random.nextInt(products.size())).getSku());
            rating.setComment(faker.lorem().sentence());
            rating.setValue(weightedRandomValue());
            String[] sources = {"site", "app", "social media"};
            rating.setSource(sources[random.nextInt(sources.length)]);
            ratingDAO.save(rating);
        }
    }

    private static int weightedRandomValue() {
        int[] values = {1, 2, 3, 4, 5};
        int[] weights = {1, 1, 3, 3, 1};

        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }
        double randomValue = Math.random() * totalWeight;
        for (int i = 0; i < weights.length; ++i) {
            randomValue -= weights[i];
            if (randomValue <= 0.0) {
                return values[i];
            }
        }
        return values[values.length - 1];
    }

    private void populateTransitions(List<String> customers, List<Product> products) {
        var faker = new Faker(new Locale("en-US"));
        Random random = new Random();
        var starts = new ArrayList<Start>();
        var stages = new ArrayList<Stage>();
        for (int i = 0; i < 500; i++) {
            var start = new Start();
            start.setCustomer(customers.get(random.nextInt(customers.size())));
            start.setSession(faker.internet().uuid());
            starts.add(startDAO.save(start));
        }
        for (int i = 0; i < 500; i++) {
            var stage = new Stage();
            String[] locations = {"home", "search filters", "product details", "account config", "cart"};
            stage.setLocation(locations[random.nextInt(locations.length)]);
            Product product = products.get(random.nextInt(products.size()));
            stage.setUrl(product.getPage());
            stages.add(stageDAO.save(stage));
        }
        Instant now = Instant.now();
        Instant oneYearAgo = now.minusSeconds(365L * 24 * 60 * 60);
        for (int i = 0; i < 500; i++) {
            var transition = new Transition();
            transition.setCustomer(starts.get(i));
            transition.setPage(stages.get(i));
            long randomTimestamp = oneYearAgo.getEpochSecond()
                    + (long) (random.nextDouble() * (now.getEpochSecond() - oneYearAgo.getEpochSecond()));
            transition.setTimestamp(Timestamp.from(Instant.ofEpochSecond(randomTimestamp)));
            transitionDAO.save(transition);
        }
    }
}
