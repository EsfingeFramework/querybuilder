package esfinge.querybuilder.mongodb;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public abstract class DatastoreProvider {

    private final Morphia morphia = new Morphia();
    protected MongoClient mongo;

    public abstract Datastore getDatastore();

    public Morphia getMorphia() {
        return morphia;
    }

    protected void mappClass(Class<?> clazz) {
        morphia.map(clazz);
    }

}
