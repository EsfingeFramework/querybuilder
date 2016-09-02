package net.sf.esfinge.querybuilder.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public abstract class DatastoreProvider {
	
	private Morphia morphia = new Morphia();
	protected MongoClient mongo;
	
	public abstract Datastore getDatastore();
	
	public Morphia getMorphia(){
		return morphia;
	}
	
	protected void mappClass(Class<?> clazz){
		morphia.map(clazz);
	}
	
}
