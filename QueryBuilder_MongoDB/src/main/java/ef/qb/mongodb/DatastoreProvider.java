package ef.qb.mongodb;

import dev.morphia.Datastore;

public interface DatastoreProvider {

    Datastore getDatastore();

    void mappClass(Class<?> clazz);

}