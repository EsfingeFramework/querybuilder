package net.sf.esfinge.querybuilder.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.mongodb.testresources.Person;
import net.sf.esfinge.querybuilder.mongodb.testresources.QueryBuilderDatabaseTest;


public class QueryBuilderMongoDBIntegrationTest extends QueryBuilderDatabaseTest {
	
	private TestQuery tq;

	@Before
	public void setupDatabase() throws Exception {
		initializeDatabase("/initial_db.xml");
		tq = QueryBuilder.create(TestQuery.class);
	}

	@Test
	public void simpleQuery(){
		List<Person> list = tq.getPerson();
		assertEquals("The list should have 5 persons", 5, list.size());
	}
	
	@Test
	public void simpleParameterQuery(){
		Person p = tq.getPersonById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}
	
	@Test
	public void listParameterQuery(){
		List<Person> list = tq.getPersonByLastName("Silva");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
		assertEquals("The second should be Marcos", "Marcos", list.get(1).getName());
	}
	
	@Test
	public void queryWithTwoAndParameters(){
		Person p = tq.getPersonByNameAndLastName("Pedro","Silva");
		assertEquals("It should get Pedro with id=1", new Integer(1), p.getId());
	}
	
	@Test
	public void queryWithTwoOrParameters(){
		List<Person> list = tq.getPersonByNameOrLastName("Maria","Silva");
		assertEquals("The list should have 3 persons", 3, list.size());
	}
	
	@Test
	public void queryWithOtherTable(){
		List<Person> list = tq.getPersonByAddressCity("Juiz de Fora");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The first should be Antonio", "Antonio", list.get(0).getName());
		assertEquals("The second should be Silvia", "Silvia", list.get(1).getName());
	}
	
	@Test
	public void compositeQueryWithOtherTable(){
		List<Person> list = tq.getPersonByLastNameAndAddressState("Silva","SP");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
		assertEquals("Marcos", list.get(1).getName()); 
	}
	
	@Test
	public void queryWithGreaterThan(){
		List<Person> list = tq.getPersonByAge(40);
		assertEquals("The list should have 2 persons", 2, list.size());
	}
	
	@Test
	public void queryWithLesserThan(){
		List<Person> list = tq.getPersonByAgeLesser(40);
		assertEquals("The list should have 3 persons", 3, list.size());
	}
	
	@Test
	public void queryWithNotEquals(){
		List<Person> list = tq.getPersonByLastNameNotEquals("Silva");
		assertEquals("The list should have 3 persons", 3, list.size());
	}
	
	@Test
	public void queryWithStringStarted(){
		List<Person> list = tq.getPersonByName("M");
		assertEquals("The list should have 2 persons", 2, list.size());
	}
	
	@Test
	public void queryWithTwoParametersWithComparisonTypes(){
		List<Person> list = tq. getPersonByNameStartsAndAgeGreater("M",30);
		assertEquals("The list should have 1 persons", 1, list.size());
	}
	
	@Test
	public void orderByQuery(){
		List<Person> list = tq. getPersonOrderByName();
		assertListOrder(list, "Antonio", "Marcos", "Maria", "Pedro", "Silvia");
	}
	
	@Test
	public void orderByWithParameter(){
		List<Person> list = tq. getPersonByAgeOrderByNameDesc(20);
		assertListOrder(list,"Silvia", "Maria", "Marcos", "Antonio");
	}
	
	private void assertListOrder(List<Person> list, String... names ){
		assertEquals(list.size(), names.length);
		for(int i=0; i<names.length; i++){
			assertEquals(list.get(i).getName(), names[i]);
		}
	}
	
}
