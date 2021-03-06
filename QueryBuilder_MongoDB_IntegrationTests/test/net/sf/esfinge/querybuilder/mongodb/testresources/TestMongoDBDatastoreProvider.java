package net.sf.esfinge.querybuilder.mongodb.testresources;

import org.mongodb.morphia.Datastore;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import net.sf.esfinge.querybuilder.mongodb.DatastoreProvider;

public class TestMongoDBDatastoreProvider extends DatastoreProvider{
	
	public TestMongoDBDatastoreProvider(){
		try {
			mongo = new MongoClient("localhost", 27017);
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

	public void resetDB(){
		getDatastore().getDB().dropDatabase();
	}
}
