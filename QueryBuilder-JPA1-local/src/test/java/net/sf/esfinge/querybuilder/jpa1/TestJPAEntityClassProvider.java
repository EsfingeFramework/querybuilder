package net.sf.esfinge.querybuilder.jpa1;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.jpa1.JPAEntityClassProvider;
import net.sf.esfinge.querybuilder.jpa1.testresources.Address;
import net.sf.esfinge.querybuilder.jpa1.testresources.Person;

public class TestJPAEntityClassProvider {
	
	@Test
	public void getEntityClass(){
		JPAEntityClassProvider provider = new JPAEntityClassProvider();
		assertEquals("Should retrieve Person", Person.class, provider.getEntityClass("Person"));
		assertEquals("Should retrieve Address", Address.class, provider.getEntityClass("Address"));
	}
	
	@Test
	public void entityClassNotFound(){
		JPAEntityClassProvider provider = new JPAEntityClassProvider();
		assertNull(provider.getEntityClass("Other"));
	}

}
