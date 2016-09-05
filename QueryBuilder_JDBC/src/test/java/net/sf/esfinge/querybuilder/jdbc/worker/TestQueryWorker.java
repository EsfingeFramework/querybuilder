package net.sf.esfinge.querybuilder.jdbc.worker;

import static org.junit.Assert.assertEquals;
import net.sf.esfinge.querybuilder.jdbc.CommandType;
import net.sf.esfinge.querybuilder.jdbc.testresources.Person;
import net.sf.esfinge.querybuilder.utils.Query;

import org.junit.Test;

public class TestQueryWorker {

	
	@Test
	public void selectAllQuery()
	{
		Query query = new Query();
		Person person = new Person();
		String generatedQuery = "";
		query.setObj(person);		
		try
		{
			query.setCommandType(CommandType.SELECT_ALL);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");
	}
	
	
	@Test
	public void selectQuerySingle()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		query.setObj(person);		
		try
		{
			query.setCommandType(CommandType.SELECT_SINGLE);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and person.address_id = address.id");
	}	
	
	@Test
	public void selectQueryExists()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		query.setObj(person);		
		try
		{
			query.setCommandType(CommandType.SELECT_EXISTS);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and person.address_id = address.id");
	}
	
	@Test
	public void selectQueryByExample()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		query.setObj(person);		
		try
		{
			query.setCommandType(CommandType.SELECT_BY_EXAMPLE);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.id = 1 and address.id = 1 and person.address_id = address.id");
	}	
	
	@Test
	public void insertQuery()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		person.setName("Rafael");
		person.setLastName("Lira");
		person.setAge(25);
		query.setObj(person);		
		try
		{
			query.setCommandType(CommandType.INSERT);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "insert into person (id,name,lastname,age,address_id) values (1,'Rafael','Lira',25,null)");
	}	
	
	@Test
	public void updateQuery()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		person.setName("Rafael");
		person.setLastName("Lira");
		person.setAge(25);
		query.setObj(person);
		
		try
		{
			query.setCommandType(CommandType.UPDATE);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "update person set name = 'Rafael', lastName = 'Lira', age = 25, ADDRESS_ID = null where id = 1");
	}
	
	@Test
	public void deleteQuery()
	{
		Query query           = new Query();
		String generatedQuery = "";		
		Person person         = new Person();
		person.setId(1);
		query.setObj(person);
		try
		{
			query.setCommandType(CommandType.DELETE);
			generatedQuery = query.buildCommand();		
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}		
		assertEquals(generatedQuery, "delete from person where id = 1");
	}	
	
	
}
