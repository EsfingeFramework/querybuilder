package esfinge.querybuilder.mongodb_tests;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import esfinge.querybuilder.mongodb.DatastoreProvider;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class TestMongoDBDatastoreProvider implements DatastoreProvider {

    private final Morphia morphia;
    protected MongoClient mongo;

    public TestMongoDBDatastoreProvider() {
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (MongoException e) {
            e.printStackTrace();
        }

        morphia = new Morphia();
        morphia.map(Person.class);
        morphia.map(Address.class);
    }

    @Override
    public Datastore getDatastore() {
        return getMorphia().createDatastore(mongo, "testdb");
    }

    @Override
    public Morphia getMorphia() {
        return morphia;
    }

    @Override
    public void mappClass(Class<?> clazz) {
        morphia.map(clazz);
    }

}
