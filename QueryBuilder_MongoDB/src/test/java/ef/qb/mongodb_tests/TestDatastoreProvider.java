package ef.qb.mongodb_tests;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.mongodb.DatastoreProvider;
import java.util.Collections;

@ServicePriority(1)
public class TestDatastoreProvider implements DatastoreProvider {

    private final Datastore datastore;

    public TestDatastoreProvider() {
        MongoClient mongo = null;
        try {
            mongo = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder
                                    -> builder.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))))
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        datastore = Morphia.createDatastore(mongo, "testdb");

        // Mapeando as classes
        datastore.getMapper().map(Person.class, Address.class);
        datastore.ensureIndexes();  // Garante que os índices são criados
    }

    @Override
    public Datastore getDatastore() {
        return datastore;
    }

    @Override
    public void mappClass(Class<?> clazz) {
        datastore.getMapper().map(clazz);
        datastore.ensureIndexes();
    }
}
