package esfinge.querybuilder.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public interface DatastoreProvider {

    Datastore getDatastore();

    Morphia getMorphia();

    void mappClass(Class<?> clazz);

}
