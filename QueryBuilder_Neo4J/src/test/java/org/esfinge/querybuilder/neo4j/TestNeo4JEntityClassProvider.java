package org.esfinge.querybuilder.neo4j;

import static org.junit.Assert.assertEquals;

import org.esfinge.querybuilder.neo4j.dynamic.Address;
import org.esfinge.querybuilder.neo4j.dynamic.Person;
import org.junit.Test;

public class TestNeo4JEntityClassProvider {
	
	@Test
	public void getEntityClass(){
		Neo4JEntityClassProvider provider = new Neo4JEntityClassProvider();
		assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
		assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));

	}
	
	@Test
	public void entityClassNotFound(){
		Neo4JEntityClassProvider provider = new Neo4JEntityClassProvider();
		assertEquals("Should retrieve null", null, provider.getEntityClass("Other"));
	}

}
