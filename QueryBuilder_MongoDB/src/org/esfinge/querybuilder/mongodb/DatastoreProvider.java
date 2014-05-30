package org.esfinge.querybuilder.mongodb;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public abstract class DatastoreProvider {
	
	private Morphia morphia = new Morphia();
	protected Mongo mongo;
	
	public abstract Datastore getDatastore();
	
	public Morphia getMorphia(){
		return morphia;
	}
	
	protected void mappClass(Class<?> clazz){
		morphia.map(clazz);
	}
	
}
