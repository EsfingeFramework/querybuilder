package org.esfinge.querybuilder.utils;

import static org.junit.Assert.*;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.methodparser.Person;
import org.junit.Test;

public class TestGetGenericRepositoryType {
	
	public interface PersonRepository extends Repository<Person>{}
	
	@Test
	public void verifyInterfaceType(){
		Class type = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(PersonRepository.class, Repository.class);
		assertEquals("The type should be person", type,Person.class);
	}

}
