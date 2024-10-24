package com.example.polyglot.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import esfinge.querybuilder.mongodb.DatastoreProvider;
import java.util.Collections;
import org.esfinge.virtuallab.demo.polyglot.Address;

public class PolyglotMongoDBDatastoreProvider implements DatastoreProvider {

    private final Datastore datastore;

    public PolyglotMongoDBDatastoreProvider() {
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
        datastore.getMapper().map(Address.class);
        datastore.ensureIndexes();
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
