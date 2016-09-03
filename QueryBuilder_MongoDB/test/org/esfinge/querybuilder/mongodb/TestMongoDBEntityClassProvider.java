package org.esfinge.querybuilder.mongodb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;

public class TestMongoDBEntityClassProvider {
	
	@Test
	public void getEntityClass(){
		MongoDBEntityClassProvider provider = new MongoDBEntityClassProvider();
		assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
		assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
	}
	
	@Test
	public void entityClassNotFound(){
		MongoDBEntityClassProvider provider = new MongoDBEntityClassProvider();
		assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
	}

}
