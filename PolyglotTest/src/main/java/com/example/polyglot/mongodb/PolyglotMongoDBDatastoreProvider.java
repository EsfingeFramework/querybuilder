package com.example.polyglot.mongodb;

import com.example.polyglot.entities.Person;
import com.example.polyglot.entities.Pessoa;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import esfinge.querybuilder.mongodb.DatastoreProvider;
import org.mongodb.morphia.Datastore;

public class PolyglotMongoDBDatastoreProvider extends DatastoreProvider {

    public PolyglotMongoDBDatastoreProvider() {
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (MongoException e) {
            e.printStackTrace();
        }

        getMorphia().map(Pessoa.class);
        getMorphia().map(Person.class);
    }

    @Override
    public Datastore getDatastore() {
        return getMorphia().createDatastore(mongo, "testdb");
    }

}
