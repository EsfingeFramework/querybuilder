package org.esfinge.querybuilder.mongodb;

import java.net.UnknownHostException;

import com.google.code.morphia.Datastore;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestMongoDBDatastoreProvider extends DatastoreProvider{
	
	public TestMongoDBDatastoreProvider(){
		try {
			mongo = new Mongo("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		getMorphia().map(Person.class);
		getMorphia().map(Address.class);
	}
	
	@Override
	public Datastore getDatastore() {
		return getMorphia().createDatastore(mongo, "testdb");
	}

}
