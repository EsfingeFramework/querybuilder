package org.esfinge.querybuilder.jpa1.integration;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.List;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.util.fileloader.DataFileLoader;
import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jpa1.testresources.Person;
import org.esfinge.querybuilder.jpa1.testresources.QueryBuilderDatabaseTest;
import org.esfinge.querybuilder.jpa1.testresources.TestEntityManagerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryJPA1IntegrationTest extends QueryBuilderDatabaseTest{
	
	
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
