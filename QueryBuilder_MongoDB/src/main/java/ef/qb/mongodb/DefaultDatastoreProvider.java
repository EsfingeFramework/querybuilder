package ef.qb.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import ef.qb.core.annotation.ServicePriority;
import java.util.Properties;

@ServicePriority(0)
public class DefaultDatastoreProvider implements DatastoreProvider {

    private Datastore datastore;

    @SuppressWarnings("CallToPrintStackTrace")
    public DefaultDatastoreProvider() {
        MongoClient client = null;
        String database = null;
        try (var input = DefaultDatastoreProvider.class.getClassLoader().getResourceAsStream("META-INF/morphia-config.properties")) {
            var properties = new Properties();
            properties.load(input);
            var uri = properties.getProperty("morphia.uri");
            database = uri.substring(uri.lastIndexOf('/') + 1);
            client = MongoClients.create(new ConnectionString(uri));
            datastore = Morphia.createDatastore(client, database);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Datastore getDatastore() {
        return datastore;
    }

    @Override
    public void mappClass(Class<?> clazz) {
    }

}
