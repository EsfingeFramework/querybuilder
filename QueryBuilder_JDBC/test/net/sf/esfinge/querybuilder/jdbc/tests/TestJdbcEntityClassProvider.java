package net.sf.esfinge.querybuilder.jdbc.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.jdbc.JDBCEntityClassProvider;
import net.sf.esfinge.querybuilder.jdbc.testresources.Address;
import net.sf.esfinge.querybuilder.jdbc.testresources.Person;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

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
