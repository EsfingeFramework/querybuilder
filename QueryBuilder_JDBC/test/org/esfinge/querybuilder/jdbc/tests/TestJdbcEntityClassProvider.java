package org.esfinge.querybuilder.jdbc.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


import org.esfinge.querybuilder.exception.EntityClassNotFoundException;
import org.esfinge.querybuilder.jdbc.JDBCEntityClassProvider;
import org.esfinge.querybuilder.jdbc.testresources.Address;
import org.esfinge.querybuilder.jdbc.testresources.Person;
import org.esfinge.querybuilder.methodparser.EntityClassProvider;
import org.junit.Test;

public class TestJdbcEntityClassProvider {
	
	@Test
	public void getEntityClass(){
		EntityClassProvider provider = new JDBCEntityClassProvider();		
		assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
		assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
	}
	
	@Test
	public void entityClassNotFound(){
		EntityClassProvider provider = new JDBCEntityClassProvider();
		assertNull(provider.getEntityClass("Other"));
	}
}
