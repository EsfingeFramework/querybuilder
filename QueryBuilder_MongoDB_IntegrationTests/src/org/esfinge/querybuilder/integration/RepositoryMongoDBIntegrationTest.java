package net.sf.esfinge.querybuilder.integration;

import static net.sf.junit.Assert.assertEquals;

import java.util.List;

import net.sf.esfinge.querybuilder.mongodb.testresources.Person;
import net.sf.esfinge.querybuilder.mongodb.testresources.QueryBuilderDatabaseTest;
import net.sf.junit.Before;
import net.sf.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;

public class RepositoryMongoDBIntegrationTest extends QueryBuilderDatabaseTest{
	
	
	@Before
	public void setupDatabase() throws Exception {
		initializeDatabase("/initial_db.xml");
	}

	
	@Test
	public void list(){
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		List<Person> list = tq.list();
		assertEquals("The list should have 5 persons", 5, list.size());
	}
	
	@Test
	public void queryByExample(){
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = new Person();
		p.setLastName("Silva");
		List<Person> list = tq.queryByExample(p);
		assertEquals("The list should have 2 persons", 2, list.size());
	}
	
	@Test
	public void getById(){
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = tq.getById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}
	
	@Test
	public void delete() throws Exception{
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		tq.delete(3);
		compareTables("/after_delete_db.xml","PERSON");
	}
	
	@Test
	public void save() throws Exception{
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = tq.getById(3);
		p.setAge(p.getAge()+1);
		tq.save(p);
		compareTables("/after_update_db.xml","PERSON");
	}
	
	@Test
	public void update() throws Exception{
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = new Person();
		p.setName("Beatriz");
		p.setLastName("Tosetti");
		p.setAge(0);
		p.setId(6);
		tq.save(p);
		compareTables("/after_insert_db.xml","PERSON");
	}
	

}
