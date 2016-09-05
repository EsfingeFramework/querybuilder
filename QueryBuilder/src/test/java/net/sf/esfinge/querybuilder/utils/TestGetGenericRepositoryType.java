package net.sf.esfinge.querybuilder.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.methodparser.Person;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;

public class TestGetGenericRepositoryType {
	
	public interface PersonRepository extends Repository<Person>{}
	
	@Test
	public void verifyInterfaceType(){
		Class type = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(PersonRepository.class, Repository.class);
		assertEquals("The type should be person", type,Person.class);
	}

}
