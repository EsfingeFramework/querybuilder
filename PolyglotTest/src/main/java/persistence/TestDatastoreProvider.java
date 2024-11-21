package persistence;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import ef.qb.mongodb.DatastoreProvider;
import java.util.Properties;

public class TestDatastoreProvider implements DatastoreProvider {

    private final Datastore datastore;

    @SuppressWarnings("CallToPrintStackTrace")
    public TestDatastoreProvider() {
        MongoClient mongo = null;
        String database = null;
        try (var input = TestDatastoreProvider.class.getClassLoader().getResourceAsStream("META-INF/morphia-config.properties")) {
            var properties = new Properties();
            properties.load(input);
            var morphiaUri = properties.getProperty("morphia.uri");
            database = morphiaUri.substring(morphiaUri.lastIndexOf('/') + 1);
            mongo = MongoClients.create(new ConnectionString(morphiaUri));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        datastore = Morphia.createDatastore(mongo, database);
    }

    @Override
    public Datastore getDatastore() {
        return datastore;
    }

    @Override
    public void mappClass(Class<?> clazz) {
    }

}
