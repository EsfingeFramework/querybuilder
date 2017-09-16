package net.sf.esfinge.querybuilder.neo4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.esfinge.querybuilder.neo4j.domain.Address;
import net.sf.esfinge.querybuilder.neo4j.domain.Person;

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
